package org.example.project.repositorio


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.example.project.repositorio.adapitadoreDeTabelas.AdapitadorEntidadeDatasDeRegistro
import org.example.project.repositorio.adapitadoreDeTabelas.AdapitadorEntidadeMauinas
import org.example.project.repositorio.adapitadoreDeTabelas.AdapitadorEntidadeProcessos
import org.example.project.repositorio.adapitadoreDeTabelas.AdapitadorEntidadeRegistroDeMaquinas
import org.example.project.repositorio.adapitadoreDeTabelas.AdapitadorEntidadeTiposDeRoupas
import org.example.project.repositorio.adapitadoreDeTabelas.Invalidador
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.dao.load
import org.jetbrains.exposed.v1.dao.with
import org.jetbrains.exposed.v1.javatime.date
import org.jetbrains.exposed.v1.javatime.time
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.time.LocalDate
import java.time.LocalTime


object Conecao{
   private val coroutineScope = CoroutineScope(Dispatchers.IO)
   private val conecacaoComBancoDeDados= Database.connect(url = "jdbc:mysql://localhost:3306/lvd",
                                                  user ="root" ,
                                                  password ="15a16b34c" ,)


    init {
        coroutineScope.launch {
            transaction {
                addLogger(StdOutSqlLogger)
                SchemaUtils.createDatabase("lvd")
                SchemaUtils.create(TabelaDeFuncoes)
                SchemaUtils.create(TabelaUsuarios)
                SchemaUtils.create(TabelaDeDatasDeRegistros)
                SchemaUtils.create(TabelaTipoDeRoupas)
                SchemaUtils.create(TabelaProcessos)
                SchemaUtils.create(TabelaDeRegistroDeMaquinas)
                SchemaUtils.create(TabelaInvalidacoes)
                exec("SET SQL_SAFE_UPDATES = 0;")
                 val tableNames = Invalidador.tableNaes
               coroutineScope.launch {   for (n in tableNames){
                    criarGatinhosInsert(n)
                    criarGatinhosUpdate(n)
                    criarGatinhosDelete(n)
                }

               }

            }

        }

    }
    suspend fun criarGatinhosUpdate (tableName:String){
        transaction {
         exec("DROP TRIGGER IF EXISTS update_${tableName}")
         exec("""
            CREATE TRIGGER update_${tableName}
            AFTER UPDATE ON $tableName
            FOR EACH ROW
            UPDATE ${TabelaInvalidacoes.tableName}
            SET ${TabelaInvalidacoes.invalidada.name} = 1
            WHERE ${TabelaInvalidacoes.nomeDaTabela.name} = '$tableName'
        """.trimIndent())
         }
        }

    suspend fun criarGatinhosDelete (tableName:String){
        transaction {
            exec("DROP TRIGGER IF EXISTS delete_${tableName}")
            exec("""
                       
                    CREATE TRIGGER delete_${tableName}   AFTER DELETE
                    ON ${tableName}
                    FOR EACH ROW 
                    UPDATE ${TabelaInvalidacoes.tableName}
                    set ${TabelaInvalidacoes.invalidada.name} = 1
                    WHERE ${TabelaInvalidacoes.nomeDaTabela.name}  ='${tableName}'    
                     
                """.trimIndent())
        }
    }

    suspend fun criarGatinhosInsert (tableName:String){
        transaction {
            exec("DROP TRIGGER IF EXISTS insert_${tableName}")
            exec("""
                    
                    
                    CREATE TRIGGER insert_${tableName}   AFTER INSERT
                    ON ${tableName} 
                    FOR EACH ROW
                    UPDATE ${TabelaInvalidacoes.tableName} set ${TabelaInvalidacoes.invalidada.name} = 1
                    WHERE ${TabelaInvalidacoes.nomeDaTabela.name}  LIKE '${tableName}'    
                        
                """.trimIndent())
        }
    }

    fun selectUsers(){
        coroutineScope.launch {
            val lista= transaction{
                EntidadeUsuarios.all().with(EntidadeUsuarios::funcao).toList()
            }
            lista.forEach {
                transaction {
                System.out.println("nome ${it.nome},funcao ${it.funcao.nomeDaFuncao}") }
            }
        }
    }
    fun getUserById(id:Int){
        coroutineScope.launch {
            val usuarios= coroutineScope.async {transaction {  EntidadeUsuarios.findById(1)?.load(EntidadeUsuarios::funcao)}  }.await()
           transaction {   System.out.println("usuario pelo id ${id} nome:${usuarios?.nome} funcao :${usuarios?.funcao?.nomeDaFuncao}")}
        }
    }

    suspend fun usuariobyName(codigo: String,hashDaSenha:String): Login{
        System.out.println("validando ligin codigo ${codigo} senha ${hashDaSenha}")
        val codigo =codigo.toInt()
        val usuario = coroutineScope.async { transaction {
            EntidadeUsuarios.find { TabelaUsuarios.codigoUsuaria eq  codigo }.toList()
        } }.await()
        if(usuario[0].senha.equals(hashDaSenha)){
            val funcao = coroutineScope.async {   transaction{EntidadeFuncoes.find{ TabelaDeFuncoes.id eq (usuario[0].codigoFuncao?:0) }.toList()}}.await()
           return Login.obterestadoPelaString(funcao[0].nomeDaFuncao)
        }
   return Login.Erro
    }
    fun fluxoDeMaquinas()= AdapitadorEntidadeMauinas.fluxoDeMaquinas()
    fun fluxoDeDatas()= AdapitadorEntidadeDatasDeRegistro.fluxoDeDatas()
    fun fluxoDeprocessos()= AdapitadorEntidadeProcessos.fluxoDeProsessos()
    fun fluxoDeTiposDeRoupas()= AdapitadorEntidadeTiposDeRoupas.fluxoTipoDeRoupa()
    fun fluxoDeRegistroDeDatasPorId(idData: Int)= AdapitadorEntidadeRegistroDeMaquinas.fluxoDeMaquinaProDatas(idData)
    suspend fun contagemDeMaquinasPorIdDaData(idData: Int): Int= coroutineScope.async { AdapitadorEntidadeDatasDeRegistro.contagemDeMaquinasPorIdDaData(idData) }.await()
    suspend fun apagarRegistroDeDatas(idData: Int): Boolean=coroutineScope.async { AdapitadorEntidadeDatasDeRegistro.apagarRegistroDeDatas(idData) }.await()
    suspend fun quantidadeDeMaquinasAtivas(idData:Int): Long = coroutineScope.async { AdapitadorEntidadeDatasDeRegistro.contagemDeComclusaoDeMaquinas(idData.toInt()) }.await()
    suspend fun  apagarRegistroDeMaquia(idRegistro: Int)= coroutineScope.async { AdapitadorEntidadeRegistroDeMaquinas.apagarRegistroDeMaquna(idRegistro) }.await()
    suspend fun  definirRegitroComoFinalizado(idRegistro: Int,dataFinalizacao: LocalDate,horario: LocalTime) =coroutineScope.async { AdapitadorEntidadeRegistroDeMaquinas.marcarMaquinaComoFinalizada(idRegistro,dataFinalizacao,horario) }.await()
    suspend fun criarNovaData(data: LocalDate)=coroutineScope.async { AdapitadorEntidadeDatasDeRegistro.criarNovaData(data) }.await()

    suspend fun obterUltimoIdDeDataDeRegistroAdicionada()=coroutineScope.async { AdapitadorEntidadeDatasDeRegistro.obeterUltimoIdeDeDataAdicionado() }.await()
    suspend fun  adicionarRegistroAoBAncoDeDados(idData: Int,
                                                 peso: Float,
                                                 idProcesso:Int,
                                                 idTipoDeroupa:Int,
                                                 codigoOpreador: Int,
                                                 idMaquina: Int,
                                                 hora: LocalTime)=
        coroutineScope.async {  AdapitadorEntidadeRegistroDeMaquinas.adicionarRegistroAoBAncoDeDados(idData=idData,
                                                                                                     peso = peso,
                                                                                                     idProcesso = idProcesso,
                                                                                                     idDoTipo = idTipoDeroupa,
                                                                                                     codOperador = codigoOpreador,
                                                                                                     idMaquina =idMaquina,
                                                                                                     hora = hora) }.await()

}
