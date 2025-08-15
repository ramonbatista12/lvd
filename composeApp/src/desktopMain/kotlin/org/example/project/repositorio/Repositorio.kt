package org.example.project.repositorio

class Repositorio {
    val conexao = Conecao
    suspend fun login(senha: String,codigo:String)=conexao.usuariobyName(hashDaSenha = senha, codigo = codigo)

}

