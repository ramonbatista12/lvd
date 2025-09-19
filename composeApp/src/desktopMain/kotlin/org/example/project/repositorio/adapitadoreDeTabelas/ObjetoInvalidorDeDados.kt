package org.example.project.repositorio.adapitadoreDeTabelas

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.example.project.repositorio.EntidadeDataRegistro
import org.example.project.repositorio.EntidadeInvalidacao
import org.example.project.repositorio.EntidadeRegistroDeMaquinas
import org.example.project.repositorio.EntidadeTabelaDeMaquinas
import org.example.project.repositorio.EntidadeTabelaProcesso
import org.example.project.repositorio.EntidadeTipoDeRoupas
import org.example.project.repositorio.adapitadoreDeTabelas.Invalidacao
import org.example.project.repositorio.TabelaDeDatasDeRegistros
import org.example.project.repositorio.TabelaDeFuncoes
import org.example.project.repositorio.TabelaDeMaquinas
import org.example.project.repositorio.TabelaDeRegistroDeMaquinas
import org.example.project.repositorio.TabelaInvalidacoes
import org.example.project.repositorio.TabelaProcessos
import org.example.project.repositorio.TabelaTipoDeRoupas
import org.example.project.repositorio.TabelaUsuarios

import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.statements.UpsertSqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import java.time.LocalDate
import java.time.LocalTime
import kotlin.collections.forEach











object Invalidador{
    val tableNaes = arrayOf<String>(
        TabelaDeDatasDeRegistros.tableName,
        TabelaDeRegistroDeMaquinas.tableName,
        TabelaDeMaquinas.tableName,
        TabelaUsuarios.tableName,
        TabelaProcessos.tableName,
        TabelaTipoDeRoupas.tableName,
        TabelaDeFuncoes.tableName )
    val invalidacoe=mutableMapOf<String, MutableList<Invalidacao>>()
    var job: Job? = SupervisorJob()
    val coroutineScope =CoroutineScope(Dispatchers.IO+job!!)
    val mutex = Mutex()
    init {
        for (n in tableNaes){
            invalidacoe.put(n,mutableListOf<Invalidacao>())
        }

        coroutineScope.launch {
            fluxoDeInvalidacoes().collect {
                System.out.println("invalidacao recebel um sinal")
                if(it.size!=0){

                    it.forEach {
                        System.out.println("invalidaccao vai invalidar  a tabela ${it.nome}")
                        invalidacoe[it.nome]?.forEach {

                            it.invalidate()
                        }
                        resetar(it)
                    }

                }

            }
        }
    }

    suspend fun register(tableName: String,objeto: Invalidacao){
        mutex.withLock {
            System.out.println("registrando o objeto $objeto como ouvinte da tabela  $tableName")
            checarLista(tableName)
            invalidacoe[tableName]?.add(objeto)
            objeto.invalidate()
        }
    }
    suspend fun unRegister(tableName: String,objeto: Invalidacao){
        mutex.withLock {
            System.out.println("removendo observador $objeto que observa a tabela $tableName ")
        invalidacoe[tableName]?.remove(objeto) }
    }
    fun checarLista(tableName: String){
        if( invalidacoe[tableName]==null) invalidacoe[tableName]=mutableListOf<Invalidacao>()
    }
    fun fluxoDeInvalidacoes()= flow<List<EntidadeInvalidacao>> {
        while (true){
            val lista =transaction {   EntidadeInvalidacao.Companion.find { TabelaInvalidacoes.invalidada eq true  }.toList()}

            emit(lista)
            delay(1000)
        }
    }.flowOn(Dispatchers.IO)
}



suspend fun resetar(invalidacao: EntidadeInvalidacao){
    transaction {
        invalidacao.invalidada = false
    }
}

