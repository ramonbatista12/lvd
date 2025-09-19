package org.example.project.repositorio.adapitadoreDeTabelas

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.example.project.repositorio.EntidadeTabelaDeMaquinas
import org.example.project.repositorio.TabelaDeMaquinas
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

object AdapitadorEntidadeMauinas{
    private val mutex =Mutex()

    private  var ouvinteFluxoDeMAquinas =0
    private val job: Job? =SupervisorJob()
    private val coroutineScope =CoroutineScope(Dispatchers.IO+job!!)





    suspend fun  adicionarNoVaMAquina(numero: String, pesoMasimo: Float, pesoMinimo: Float)=
        mutex.withLock { coroutineScope.async { EntidadeTabelaDeMaquinas.Companion.new { {
            this.pesoMinimo=pesoMinimo
            this.pesoMasimo =pesoMasimo
            this.numeroDaMaquina =numero.toInt()
        } } }.await()  }

    suspend fun alterarMaquina(idMaquina:Int,pesoMasimo: Float,pesoMinimo: Float,numero: Int)=mutex.withLock {
        coroutineScope.async {
            transaction {
                TabelaDeMaquinas.update({ TabelaDeMaquinas.id eq idMaquina}){
                    it[TabelaDeMaquinas.numeroDaMaquina]=numero
                    it[TabelaDeMaquinas.pesoMasimo]=pesoMasimo
                    it[TabelaDeMaquinas.pesoMinimo]=pesoMinimo

                }} }.await()
    }
    fun fluxoDeMaquinas()=flow<List<EntidadeTabelaDeMaquinas>> {
        coroutineScope {
            val  canalDeAvisos =Channel<Unit>(Channel.CONFLATED)
            val objetoObservador =object : Invalidacao {
                override fun invalidate() {
                    canalDeAvisos.trySend(Unit)
                }
            }
            canalDeAvisos.trySend(Unit)
            val canalResposta =Channel<List<EntidadeTabelaDeMaquinas>>()
            launch {
                Invalidador.register(TabelaDeMaquinas.tableName,objetoObservador)
                try {
                    for (a in canalDeAvisos){
                        val list =async { transaction{ EntidadeTabelaDeMaquinas.Companion.all().toList()} }.await()
                        canalResposta.send(list)
                    }
                }finally {
                    Invalidador.unRegister(TabelaDeMaquinas.tableName,objetoObservador)
                }


            }
            emitAll(canalResposta)

        }
    }
}