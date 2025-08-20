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


sealed class RespostaDeContagemDeMaquinas(){
    class Contagem(val quantidae: Int): RespostaDeContagemDeMaquinas()
    object Load: RespostaDeContagemDeMaquinas()
    class Erro(val mensagem : String): RespostaDeContagemDeMaquinas()

    override fun toString(): String
       =when(val r =this){
            is Erro -> r.mensagem
            is Contagem -> r.quantidae.toString()
            is Load -> "Caregando"
        }

}