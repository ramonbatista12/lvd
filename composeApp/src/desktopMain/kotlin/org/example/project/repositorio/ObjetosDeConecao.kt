package org.example.project.repositorio

import com.mysql.cj.conf.ConnectionUrlParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
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

    suspend fun contagemDeMaquinasPorIdDaData(idData: Int) =mutex.withLock {   transaction { EntidadeRegistroDeMaquinas.find { TabelaDeRegistroDeMaquinas.data eq idData }.count().toInt() }}
    suspend fun contagemDeComclusaoDeMaquinas(idData:Int)= mutex.withLock {  transaction { EntidadeRegistroDeMaquinas.find { TabelaDeRegistroDeMaquinas.finalizada eq(false) }.count() } }
    fun fluxoDeDatas()=flow<List<EntidadeDataRegistro>> {
        coroutineScope {
            val  canalDeAvisos =Channel<Unit>(Channel.CONFLATED)
            val objetoObservador =object : Invalidacao{
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
                        val list =async {  transaction{EntidadeDataRegistro.all().orderBy(Pair(TabelaDeDatasDeRegistros.id,SortOrder.DESC)).toList()} }.await()
                        canalResposta.send(list)
                    }
                }finally {
                    Invalidador.unRegister(TabelaDeDatasDeRegistros.tableName,objetoObservador)
                }


            }
            emitAll(canalResposta)

        }
    }



}

object AdapitadorEntidadeRegistroDeMaquinas{

      private val mutex =Mutex()
      private val job: Job? = SupervisorJob()
      private val coroutineScope =CoroutineScope(Dispatchers.IO)



    fun fluxoDeMaquinaProDatas(idData: Int)=flow<List<EntidadeRegistroDeMaquinas>> {
        coroutineScope {
             val canalDeAvisos = Channel<Unit>(Channel.CONFLATED)
            val objetoObservadorDeInvalidacoes =object : Invalidacao{
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
                        EntidadeRegistroDeMaquinas.find { TabelaDeRegistroDeMaquinas.data eq idData }.orderBy(
                            Pair(TabelaDeRegistroDeMaquinas.data, SortOrder.DESC)
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
                                         transaction { TabelaDeRegistroDeMaquinas.update(where = { TabelaDeRegistroDeMaquinas.id eq idRegistro}){
                                                                     it[TabelaDeRegistroDeMaquinas.finalizada]=true
                                                                     it[TabelaDeRegistroDeMaquinas.horaSaida]=hora
                                                                     it[TabelaDeRegistroDeMaquinas.dataFinalizacao]=dataFinalizacao
                                                              } } }



}

object AdapitadorEntidadeMauinas{
    private val mutex =Mutex()

    private  var ouvinteFluxoDeMAquinas =0
    private val job: Job? =SupervisorJob()
    private val coroutineScope =CoroutineScope(Dispatchers.IO+job!!)





    suspend fun  adicionarNoVaMAquina(numero: String, pesoMasimo: Float, pesoMinimo: Float)=
         mutex.withLock { coroutineScope.async { EntidadeTabelaDeMaquinas.new { {
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
            val objetoObservador =object : Invalidacao{
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
                        val list =async { transaction{EntidadeTabelaDeMaquinas.all().toList()} }.await()
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

object AdapitadorEntidadeProcessos{

    private val coroutineScope =CoroutineScope(Dispatchers.IO)
    private val mutex =Mutex()


    fun fluxoDeProsessos()=flow<List<EntidadeTabelaProcesso>> {
        coroutineScope {
            val  canalDeAvisos =Channel<Unit>(Channel.CONFLATED)
            val objetoObservador =object : Invalidacao{
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
                         val list =async { transaction{EntidadeTabelaProcesso.all().toList()} }.await()
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

object AdapitadorEntidadeTiposDeRoupas{

    private val coroutineScope=CoroutineScope(Dispatchers.IO)
    private val mutex= Mutex()

    fun fluxoTipoDeRoupa()=flow<List<EntidadeTipoDeRoupas>> {
        coroutineScope {
            val  canalDeAvisos =Channel<Unit>(Channel.CONFLATED)
            val objetoObservador =object : Invalidacao{
                override fun invalidate() {
                    canalDeAvisos.trySend(Unit)
                }
            }
            canalDeAvisos.trySend(Unit)
            val canalResposta =Channel<List< EntidadeTipoDeRoupas>>()
            launch {
                Invalidador.register(TabelaTipoDeRoupas.tableName,objetoObservador)
                try {
                    for (a in canalDeAvisos){
                        val list =async { transaction{ EntidadeTipoDeRoupas.all().toList()} }.await()
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