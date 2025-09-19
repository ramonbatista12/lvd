package org.example.project.repositorio.adapitadoreDeTabelas

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.example.project.repositorio.EntidadeRegistroDeMaquinas
import org.example.project.repositorio.TabelaDeDatasDeRegistros
import org.example.project.repositorio.TabelaDeRegistroDeMaquinas
import org.example.project.repositorio.TabelaDeRegistroDeMaquinas.dataFinalizacao
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.core.statements.UpsertSqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import java.time.LocalDate
import java.time.LocalTime

object AdapitadorEntidadeRegistroDeMaquinas{

    private val mutex =Mutex()
    private val job: Job? = SupervisorJob()
    private val coroutineScope =CoroutineScope(Dispatchers.IO)



    fun fluxoDeMaquinaProDatas(idData: Int)=flow<List<EntidadeRegistroDeMaquinas>> {
        coroutineScope {
            val canalDeAvisos = Channel<Unit>(Channel.CONFLATED)
            val objetoObservadorDeInvalidacoes =object : Invalidacao {
                override fun invalidate() {
                    System.out.println("objeto anonimo no fluxo esta recebendo  uma invalidacao")
                    canalDeAvisos.trySend(Unit)
                }
            }
            canalDeAvisos.send(Unit)
            val  canalResposta= Channel<List<EntidadeRegistroDeMaquinas>>()

            launch {
                try {


                    Invalidador.register(TabelaDeRegistroDeMaquinas.tableName, objetoObservadorDeInvalidacoes)

                    for (n in canalDeAvisos) {

                        System.out.println("aviso no canal ${n} id de data observado ${idData}")
                        val lista = transaction {
                            EntidadeRegistroDeMaquinas.Companion.find { TabelaDeRegistroDeMaquinas.data eq idData }.orderBy(
                                Pair(TabelaDeRegistroDeMaquinas.id, SortOrder.DESC)
                            ).toList()
                        }
                        delay(1000)
                        canalResposta.send(lista)
                    }
                } finally {
                    Invalidador.unRegister(TabelaDeDatasDeRegistros.tableName, objetoObservadorDeInvalidacoes)
                    System.out.println("finalizacao corey aonde se cria os fluxos para o id $idData")
                }


            }

            emitAll(canalResposta)
        }

    }

    suspend fun apagarRegistroDeMaquna(idRegistro:Int)= mutex.withLock { transaction { TabelaDeRegistroDeMaquinas.deleteWhere { TabelaDeRegistroDeMaquinas.id eq idRegistro } } }
    suspend fun marcarMaquinaComoFinalizada(idRegistro: Int,data: LocalDate,hora: LocalTime) =mutex.withLock {
        transaction {
            System.out.println("acao de definir registro como finalizado query")
            addLogger(StdOutSqlLogger)
            TabelaDeRegistroDeMaquinas.update(where = { TabelaDeRegistroDeMaquinas.id eq idRegistro}){
            it[TabelaDeRegistroDeMaquinas.finalizada]=true
            it[TabelaDeRegistroDeMaquinas.horaSaida]=hora
            it[TabelaDeRegistroDeMaquinas.dataFinalizacao]=data
        } } }

    suspend fun adicionarRegistroAoBAncoDeDados(idData:Int,peso:Float,idProcesso:Int,idDoTipo:Int,codOperador:Int,idMaquina: Int,hora: LocalTime)=mutex.withLock {
        transaction {
            TabelaDeRegistroDeMaquinas.insert { collunDescriptor->
                collunDescriptor[TabelaDeRegistroDeMaquinas.data]=idData
                collunDescriptor[TabelaDeRegistroDeMaquinas.maquina]=idMaquina
                collunDescriptor[TabelaDeRegistroDeMaquinas.processo]=idProcesso
                collunDescriptor[TabelaDeRegistroDeMaquinas.horaEntrada]=hora
                collunDescriptor[TabelaDeRegistroDeMaquinas.peso]=peso
                collunDescriptor[TabelaDeRegistroDeMaquinas.tipoDeRoupa]=idDoTipo
                collunDescriptor[TabelaDeRegistroDeMaquinas.codigoDoOperar]=codOperador
                collunDescriptor[TabelaDeRegistroDeMaquinas.horaSaida]=null
                collunDescriptor[TabelaDeRegistroDeMaquinas.dataFinalizacao]=null
                collunDescriptor[TabelaDeRegistroDeMaquinas.finalizada]=false

            }
        }
    }



}