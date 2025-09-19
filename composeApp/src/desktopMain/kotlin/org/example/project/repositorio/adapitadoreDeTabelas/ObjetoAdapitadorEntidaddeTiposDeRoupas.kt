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
import org.example.project.repositorio.EntidadeTipoDeRoupas
import org.example.project.repositorio.TabelaTipoDeRoupas
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object AdapitadorEntidadeTiposDeRoupas{

    private val coroutineScope=CoroutineScope(Dispatchers.IO)
    private val mutex= Mutex()

    fun fluxoTipoDeRoupa()=flow<List<EntidadeTipoDeRoupas>> {
        coroutineScope {
            val  canalDeAvisos =Channel<Unit>(Channel.CONFLATED)
            val objetoObservador =object : Invalidacao {
                override fun invalidate() {
                    canalDeAvisos.trySend(Unit)
                }
            }
            canalDeAvisos.trySend(Unit)
            val canalResposta =Channel<List<EntidadeTipoDeRoupas>>()
            launch {
                Invalidador.register(TabelaTipoDeRoupas.tableName,objetoObservador)
                try {
                    for (a in canalDeAvisos){
                        val list =async { transaction{ EntidadeTipoDeRoupas.Companion.all().toList()} }.await()
                        canalResposta.send(list)
                    }
                }finally {
                    Invalidador.unRegister(TabelaTipoDeRoupas.tableName,objetoObservador)
                }


            }
            emitAll(canalResposta)

        }
    }

}