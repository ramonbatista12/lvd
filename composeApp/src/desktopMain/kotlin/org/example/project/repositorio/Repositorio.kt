package org.example.project.repositorio

import org.jetbrains.skia.Data

object Repositorio {
    val conexao = Conecao
    suspend fun login(senha: String,codigo:String)=conexao.usuariobyName(hashDaSenha = senha, codigo = codigo)
    fun fluxoDeDatasDeRegistro() = conexao.fluxoDeDatas()
    fun fluxoDeregistroPorDatas(idData: Int)=conexao.fluxoDeRegistroDeDatasPorId(idData)

    suspend fun contagemDeMaquinasPorIdDaData(idData: Int) =conexao.contagemDeMaquinasPorIdDaData(idData)

    suspend fun apagarUmRegistroDeDatas(idData:Int)=conexao.apagarRegistroDeDatas(idData)
    suspend fun quantidadeDeMaquianssAtivas(idData: Int)=conexao.quantidadeDeMaquinasAtivas(idData)

}

