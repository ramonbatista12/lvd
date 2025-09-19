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
import org.example.project.repositorio.EntidadeDataRegistro
import org.example.project.repositorio.EntidadeRegistroDeMaquinas
import org.example.project.repositorio.TabelaDeDatasDeRegistros
import org.example.project.repositorio.TabelaDeRegistroDeMaquinas
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.statements.UpsertSqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.time.LocalDate

object  AdapitadorEntidadeDatasDeRegistro{


    private val mutex = Mutex()
    private val Job:Job?= SupervisorJob()
    private val corotineScope = CoroutineScope(Dispatchers.IO+Job!!)



    suspend fun  apagarRegistroDeDatas(idData: Int): Boolean{
        val linhasApagadas=  transaction {
            TabelaDeDatasDeRegistros.deleteWhere{ TabelaDeDatasDeRegistros.id eq  idData}}

        if(linhasApagadas>0) return true
        return  false

    }

    suspend fun contagemDeMaquinasPorIdDaData(idData: Int) =mutex.withLock {   transaction {
        System.out.println("query que conta as maquinas na tabela registroDemaquina para o id $idData")
        addLogger(StdOutSqlLogger)
        EntidadeRegistroDeMaquinas.Companion.find { TabelaDeRegistroDeMaquinas.data eq idData }.count().toInt() }}
    suspend fun contagemDeComclusaoDeMaquinas(idData:Int)= mutex.withLock {  transaction {
        System.out.println("query cintagen de maquinas ativas")
        addLogger(StdOutSqlLogger)
        EntidadeRegistroDeMaquinas.Companion.find { (TabelaDeRegistroDeMaquinas.data eq idData) and (TabelaDeRegistroDeMaquinas.dataFinalizacao eq null) }.count() } }
    suspend fun criarNovaData(d: LocalDate): EntidadeDataRegistro {
        mutex.withLock {
            val entidade= transaction { EntidadeDataRegistro.Companion.new {
                data=d
                esado =false
            }
            }
            return entidade
        }

    }

    fun fluxoDeDatas()=flow<List<EntidadeDataRegistro>> {
        coroutineScope {
            val  canalDeAvisos =Channel<Unit>(Channel.CONFLATED)
            val objetoObservador =object : Invalidacao {
                override fun invalidate() {
                    canalDeAvisos.trySend(Unit)
                }
            }
            canalDeAvisos.trySend(Unit)
            val canalResposta =Channel<List<EntidadeDataRegistro>>()
            launch {
                Invalidador.register(TabelaDeDatasDeRegistros.tableName,objetoObservador)
                try {
                    for (a in canalDeAvisos){
                        val list =async {  transaction{
                            EntidadeDataRegistro.Companion.all().orderBy(Pair(
                                TabelaDeDatasDeRegistros.id,SortOrder.DESC)).toList()} }.await()
                        canalResposta.send(list)
                    }
                }finally {
                    Invalidador.unRegister(TabelaDeDatasDeRegistros.tableName,objetoObservador)
                }


            }
            emitAll(canalResposta)

        }
    }
    suspend fun obeterUltimoIdeDeDataAdicionado()=
        transaction {
            val entidaede = EntidadeDataRegistro.Companion.all().limit(1).orderBy(Pair(TabelaDeDatasDeRegistros.id, SortOrder.DESC)).toList()
            return@transaction entidaede[0]
        }



}