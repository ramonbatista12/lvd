package org.example.project.repositorio

import org.jetbrains.exposed.v1.javatime.Time
import org.jetbrains.skia.Data
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date
import java.util.Timer

object Repositorio {
    val conexao = Conecao
    suspend fun login(senha: String,codigo:String)=conexao.usuariobyName(hashDaSenha = senha, codigo = codigo)
    fun fluxoDeDatasDeRegistro() = conexao.fluxoDeDatas()
    fun fluxoDeregistroPorDatas(idData: Int)=conexao.fluxoDeRegistroDeDatasPorId(idData)
    fun fluxoDeMaquinas()= conexao.fluxoDeMaquinas()
    fun fluxoDeProcessos()=conexao.fluxoDeprocessos()
    fun fluxoTipoDeRoupas()=conexao.fluxoDeTiposDeRoupas()

    suspend fun contagemDeMaquinasPorIdDaData(idData: Int) =conexao.contagemDeMaquinasPorIdDaData(idData)

    suspend fun apagarUmRegistroDeDatas(idData:Int)=conexao.apagarRegistroDeDatas(idData)
    suspend fun quantidadeDeMaquianssAtivas(idData: Int)=conexao.quantidadeDeMaquinasAtivas(idData)

    suspend fun definirRegistroComoFinalizado(idData: Int,dataFinalizacao: LocalDate,horario: LocalTime) =conexao.definirRegitroComoFinalizado(idData,dataFinalizacao,horario)
    suspend fun  apagarRegistroDeMaquinaPeloId(idDoRegistro: Int)= conexao.apagarRegistroDeMaquia(idDoRegistro)
}

