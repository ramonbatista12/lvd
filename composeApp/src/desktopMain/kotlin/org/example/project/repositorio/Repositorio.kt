package org.example.project.repositorio

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.jetbrains.exposed.v1.javatime.Time
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.skia.Data
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date
import java.util.Timer

object Repositorio {
    private val conexao = Conecao
    suspend fun login(senha: String,codigo:String)=conexao.usuariobyName(hashDaSenha = senha, codigo = codigo)
    //fluxos
    fun fluxoDeDatasDeRegistro() = conexao.fluxoDeDatas().map {
        it.map {
            DataDeRegistro(it.id_data.value,it.data)
        }
    }
    fun fluxoDeregistroPorDatas(idData: Int)=conexao.fluxoDeRegistroDeDatasPorId(idData).map {
         it.map {

             transaction {
                 System.out.println("mapeando emicao do fluxo da entidade registro de maquinas ${it.codOperador}")

             RegistroDeMaquinasPorId(idRegistro = it.idRegistro.value,
                                     idData = it.idData,
                                     cod=it.codOperador,
                                     idProcess = it.idProcesso,
                                     idMaquina = it.maquina,
                                     peso = it.peso,
                                     dataF = it.dataFinalizacao,
                                     horaE = it.entrada,
                                     horaS = it.saida,
                                     nomeOp = it.operador.nome,
                                     nomeProces = it.processo.nome .toString(),
                                     nomeTipoRoupa = it.tipo.nome,
                                     numeroDaMAquina = it.maquinaUsada.numeroDaMaquina.toString()) }
         }
    }.buffer()
    fun fluxoDeMaquinas()= conexao.fluxoDeMaquinas()
    fun fluxoDeProcessos()=conexao.fluxoDeprocessos()
    fun fluxoTipoDeRoupas()=conexao.fluxoDeTiposDeRoupas()
    //acoes em maquinas por id data
    suspend fun contagemDeMaquinasPorIdDaData(idData: Int) =conexao.contagemDeMaquinasPorIdDaData(idData)

    suspend fun apagarUmRegistroDeDatas(idData:Int)=conexao.apagarRegistroDeDatas(idData)
    suspend fun quantidadeDeMaquianssAtivas(idData: Int)=conexao.quantidadeDeMaquinasAtivas(idData)

    suspend fun definirRegistroComoFinalizado(idData: Int,dataFinalizacao: LocalDate,horario: LocalTime) =conexao.definirRegitroComoFinalizado(idData,dataFinalizacao,horario)
    suspend fun  apagarRegistroDeMaquinaPeloId(idDoRegistro: Int)= conexao.apagarRegistroDeMaquia(idDoRegistro)


    //acoes em dataRegistro
    suspend fun criarNovaData(data: LocalDate)=conexao.criarNovaData(data)
    suspend fun obeterUltimoIdAdicionado()=conexao.obterUltimoIdDeDataDeRegistroAdicionada()
    suspend fun  adicionarRegistroDeMaquinasAoBAncoDeDados(idData: Int,peso: Float,idProcesso:Int,idTipo:Int,codOpera:Int,idMaquina: Int,hora: LocalTime)
    =conexao.adicionarRegistroAoBAncoDeDados(idData = idData, peso = peso, idTipoDeroupa = idTipo, codigoOpreador = codOpera, idMaquina = idMaquina, hora = hora, idProcesso = idProcesso)
}

