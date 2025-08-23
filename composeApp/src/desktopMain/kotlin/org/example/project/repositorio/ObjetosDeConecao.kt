package org.example.project.repositorio

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.statements.UpsertSqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import java.time.LocalDate
import java.time.LocalTime

object  AdapitadorEntidadeDatasDeRegistro: Invalidacao{
     private var recursoDePaginacaoDeDatasDeRegistroDeMaquinas: RecursoDePaginacaoDeDatasDeRegistroDeMaquinas?=null
    val fluxoPaginadoDeDatasDeRegistroDeMaquinas = MutableStateFlow<List<EntidadeDataRegistro>>(emptyList())
    var contagemDeOuvintes =0
    private val mutex = Mutex()
    private val Job:Job?= SupervisorJob()
    private val corotineScope = CoroutineScope(Dispatchers.IO+Job!!)


    init {

        corotineScope.launch{
            fluxoPaginadoDeDatasDeRegistroDeMaquinas.subscriptionCount.collect {
                contagemDeOuvintes=it

                    System.out.println("ouvintes do fluxo de datas de registros ${it}")
                    invalidate()
                if(it==0){
                    fluxoPaginadoDeDatasDeRegistroDeMaquinas.emit(emptyList())
                }


            }
        }
        corotineScope.launch(Dispatchers.Default) {
            Invalidador.register(TabelaDeDatasDeRegistros.tableName,this@AdapitadorEntidadeDatasDeRegistro)
        }

    }

    suspend fun  apagarRegistroDeDatas(idData: Int): Boolean{

        val linhasApagadas=  transaction {
                TabelaDeDatasDeRegistros.deleteWhere{ TabelaDeDatasDeRegistros.id eq  idData}

            }


        return  true

    }

    suspend fun contagemDeMaquinasPorIdDaData(idData: Int) =mutex.withLock {   transaction { EntidadeRegistroDeMaquinas.find { TabelaDeRegistroDeMaquinas.data eq idData }.count().toInt() }}
    suspend fun contagemDeComclusaoDeMaquinas(idData:Int)= mutex.withLock {  transaction { EntidadeRegistroDeMaquinas.find { TabelaDeRegistroDeMaquinas.finalizada eq(false) }.count() } }
override fun invalidate() {

        if(contagemDeOuvintes>0)
        corotineScope.launch {
            System.out.println("invalidate chamado em Adapitador de datas de registro ")
            val lista =transaction { EntidadeDataRegistro.all().orderBy(Pair(TabelaDeDatasDeRegistros.id, SortOrder.DESC)).toList() }
            fluxoPaginadoDeDatasDeRegistroDeMaquinas.emit(lista)
            System.out.println("fluxo emitido")
        }

    }
}

object AdapitadorEntidadeRegistroDeMaquinas: Invalidacao{
       val fluxoDeDados =MutableStateFlow<List<EntidadeRegistroDeMaquinas>>(emptyList())
      var idData =0
      var ouvintes =0
      private val mutex =Mutex()
      private val job: Job? = SupervisorJob()
      private val coroutineScope =CoroutineScope(Dispatchers.IO)
    init {
        coroutineScope.launch(Dispatchers.Default){
            fluxoDeDados.subscriptionCount.collect {
                ouvintes=it
                if(it>0){
                    invalidate()
                }
                if(it==0){
                    fluxoDeDados.emit(emptyList())
                }
            }
        }
        coroutineScope.launch {
            Invalidador.register(TabelaDeRegistroDeMaquinas.tableName,this@AdapitadorEntidadeRegistroDeMaquinas)
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

    suspend fun apagarRegistroDeMaquna(idRegistro:Int)= mutex.withLock { transaction { TabelaDeRegistroDeMaquinas.deleteWhere { TabelaDeRegistroDeMaquinas.id eq idRegistro } } }
    suspend fun marcarMaquinaComoFinalizada(idRegistro: Int,data: LocalDate,hora: LocalTime) =mutex.withLock {
                                         transaction { TabelaDeRegistroDeMaquinas.update(where = { TabelaDeRegistroDeMaquinas.id eq idRegistro}){
                                                                     it[TabelaDeRegistroDeMaquinas.finalizada]=true
                                                                     it[TabelaDeRegistroDeMaquinas.horaSaida]=hora
                                                                     it[TabelaDeRegistroDeMaquinas.dataFinalizacao]=dataFinalizacao
                                                              } } }
    override fun invalidate() {
        coroutineScope.launch{
            System.out.println("invalidete chamado no Adapitador entidade registro de dados")
            val lista=async {
                transaction { EntidadeRegistroDeMaquinas.find { TabelaDeRegistroDeMaquinas.data eq idData }.orderBy(Pair(
                    TabelaDeRegistroDeMaquinas.data, SortOrder.DESC)).toList() }
            }.await()
            fluxoDeDados.emit(lista)
        }
    }
}

object AdapitadorEntidadeMauinas: Invalidacao{
    private val mutex =Mutex()
    val fluxoDeMaquinas =MutableStateFlow<List<EntidadeTabelaDeMaquinas>>(emptyList())
    private  var ouvinteFluxoDeMAquinas =0
    private val job: Job? =SupervisorJob()
    private val coroutineScope =CoroutineScope(Dispatchers.IO+job!!)

    init {
        coroutineScope.launch(Dispatchers.Default) {
            coroutineScope.launch {
                fluxoDeMaquinas.subscriptionCount.collect {
                    ouvinteFluxoDeMAquinas=it
                    invalidate()
                    if(it==0){
                        fluxoDeMaquinas.emit(emptyList())
                    }
                }}
            launch {
                Invalidador.register(TabelaDeMaquinas.tableName,this@AdapitadorEntidadeMauinas)
            }
        }
    }
    override fun invalidate() {
        if (ouvinteFluxoDeMAquinas>0)
       coroutineScope.launch {
           val lista =async { EntidadeTabelaDeMaquinas.all().toList() }.await()
           fluxoDeMaquinas.emit(lista)
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
            checarLista(tableName)
            invalidacoe[tableName]?.add(objeto)
        }
    }
    suspend fun unRegister(tableName: String,objeto: Invalidacao){
        mutex.withLock {
        invalidacoe[tableName]?.remove(objeto) }
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