package org.example.project.repositorio

sealed class Login(val nome:String){
    object Desconectado: Login("desconectado")
    object Alsiliar: Login("Alsiliar")
    object Operador: Login("Operador")
    object Gerente:Login("Gerencia")

    object Erro: Login("")

    companion object{
        suspend fun obterestadoPelaString(string: String) =when{
            string== Alsiliar.nome -> Alsiliar
            string== Operador.nome-> Operador
            string== Gerente.nome-> Gerente
            else -> Erro
        }
    }
    override fun toString(): String =this.nome
}