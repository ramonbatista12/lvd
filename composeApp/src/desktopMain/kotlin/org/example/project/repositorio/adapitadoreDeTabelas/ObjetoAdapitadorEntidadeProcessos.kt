package org.example.project.repositorio.adapitadoreDeTabelas

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import org.example.project.repositorio.EntidadeTabelaProcesso
import org.example.project.repositorio.TabelaProcessos
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object AdapitadorEntidadeProcessos{

    private val coroutineScope =CoroutineScope(Dispatchers.IO)
    private val mutex =Mutex()


    fun fluxoDeProsessos()=flow<List<EntidadeTabelaProcesso>> {
        coroutineScope {
            val  canalDeAvisos =Channel<Unit>(Channel.CONFLATED)
            val objetoObservador =object : Invalidacao {
                override fun invalidate() {
                    canalDeAvisos.trySend(Unit)
                }
            }
            canalDeAvisos.trySend(Unit)
            val canalResposta =Channel<List<EntidadeTabelaProcesso>>()
            launch {
                Invalidador.register(TabelaProcessos.tableName,objetoObservador)
                try {
                    for (a in canalDeAvisos){
                        val list =async { transaction{ EntidadeTabelaProcesso.Companion.all().toList()} }.await()
                        canalResposta.send(list)
                    }
                }finally {
                    Invalidador.unRegister(TabelaProcessos.tableName,objetoObservador)
                }


            }
            emitAll(canalResposta)

        }
    }

}