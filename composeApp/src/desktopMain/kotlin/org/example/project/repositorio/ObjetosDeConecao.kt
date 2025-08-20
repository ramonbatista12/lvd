package org.example.project.repositorio

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.vector.PathData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import app.cash.paging.PagingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.example.project.componentesComponiveis.Paineis.componentesRegistroDeMaquinas.PaginasDoPainel

import org.example.project.repositorio.Conecao.uimaPagngSorceDatasDeRegistroDeMaquinas
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.StdDevPop
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.core.statements.UpsertSqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object  AdapitadorEntidadeDatasDeRegistro: Invalidacao{
     private var recursoDePaginacaoDeDatasDeRegistroDeMaquinas: RecursoDePaginacaoDeDatasDeRegistroDeMaquinas?=null
    val fluxoPaginadoDeDatasDeRegistroDeMaquinas = MutableStateFlow<List<EntidadeDataRegistro>>(emptyList())
    var contagemDeOuvintes =0


    init {

        CoroutineScope(Dispatchers.Default).launch{
            fluxoPaginadoDeDatasDeRegistroDeMaquinas.subscriptionCount.collect {
                contagemDeOuvintes=it

                    System.out.println("ouvintes do fluxo de datas de registros ${it}")
                    invalidate()


            }
        }
        CoroutineScope(Dispatchers.Default).launch {
            Invalidador.register(TabelaDeDatasDeRegistros.tableName,this@AdapitadorEntidadeDatasDeRegistro)
        }

    }

    suspend fun  apagarRegistroDeDatas(idData: Int): Boolean{

        val linhasApagadas=  transaction {
                TabelaDeDatasDeRegistros.deleteWhere{ TabelaDeDatasDeRegistros.id eq  idData}

            }


        return  true

    }

    suspend fun contagemDeMaquinasPorIdDaData(idData: Int) =transaction { EntidadeRegistroDeMaquinas.find { TabelaDeRegistroDeMaquinas.data eq idData }.groupBy { TabelaDeRegistroDeMaquinas.data }.count() }
    suspend fun contagemDeComclusaoDeMaquinas(idData:Int)=transaction { EntidadeRegistroDeMaquinas.find { TabelaDeRegistroDeMaquinas.finalizada eq(false) }.count() }
    override fun invalidate() {

        if(contagemDeOuvintes>0)
        CoroutineScope(Dispatchers.IO).launch {
            val lista =transaction { EntidadeDataRegistro.all().orderBy(Pair(TabelaDeDatasDeRegistros.id, SortOrder.DESC)).toList() }
            fluxoPaginadoDeDatasDeRegistroDeMaquinas.emit(lista)
        }

    }
}

object AdapitadorEntidadeRegistroDeMaquinas: Invalidacao{
       val fluxoDeDados =MutableStateFlow<List<EntidadeRegistroDeMaquinas>>(emptyList())
      var idData =0
      var ouvintes =0
    init {
        CoroutineScope(Dispatchers.Default).launch{
            fluxoDeDados.subscriptionCount.collect {
                ouvintes=it
                if(it>1){
                    invalidate()
                }
                if(it==0){
                    fluxoDeDados.emit(emptyList())
                }
            }
        }
    }
    fun fluxoDeMaquinasProDatas(idData: Int): MutableStateFlow<List<EntidadeRegistroDeMaquinas>>{
           this.idData= idData
        CoroutineScope(Dispatchers.Default).launch{
            Invalidador.register(TabelaDeRegistroDeMaquinas.tableName,this@AdapitadorEntidadeRegistroDeMaquinas)
            invalidate()
        }
        return fluxoDeDados
    }

    override fun invalidate() {
        CoroutineScope(Dispatchers.IO).launch{
            val lista=async {
                transaction { EntidadeRegistroDeMaquinas.find { TabelaDeRegistroDeMaquinas.data eq idData }.orderBy(Pair(
                    TabelaDeRegistroDeMaquinas.data, SortOrder.DESC)).toList() }
            }.await()
            fluxoDeDados.emit(lista)
        }
    }
}



object Invalidador{
    val tableNaes = arrayOf<String>(TabelaDeDatasDeRegistros.tableName,
                                    TabelaDeRegistroDeMaquinas.tableName,
                                    TabelaDeMaquinas.tableName,
                                    TabelaUsuarios.tableName,
                                    TabelaProcessos.tableName,
                                    TabelaTipoDeRoupas.tableName,
                                    TabelaDeFuncoes.tableName )
    val invalidacoe=mutableMapOf<String, MutableList<Invalidacao>>()

    val mutex = Mutex()
    init {
        for (n in tableNaes){
        invalidacoe.put(n,mutableListOf<Invalidacao>())
        }

        CoroutineScope(Dispatchers.Default).launch {
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
         checarLista(tableName)
        invalidacoe[tableName]?.add(objeto)
        }
    }
   suspend fun unRegister(tableName: String,objeto: Invalidacao){
            invalidacoe[tableName]?.remove(objeto)
    }
    fun checarLista(tableName: String){
        if( invalidacoe[tableName]==null) invalidacoe[tableName]=mutableListOf<Invalidacao>()
    }
    fun fluxoDeInvalidacoes()= flow<List<EntidadeInvalidacao>> {
          while (true){
             val lista =transaction {   EntidadeInvalidacao.find { TabelaInvalidacoes.invalidada eq true  }.toList()}
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

interface Invalidacao{
    fun invalidate()
}