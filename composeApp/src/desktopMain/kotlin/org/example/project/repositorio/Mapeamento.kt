package org.example.project.repositorio

import java.time.LocalDate
import java.time.LocalTime

data class TipoRoupa(val tipo:String,val descricao:String,val id:Int)

data class funcionario(val id: Int,val funcao: Funcoes,val codigo: Int,val nome:String)

data class DataDeRegistro(val id: Int,val dataRegistro: LocalDate)

data class RegistroDeMaquinasPorId(val idRegistro:Int,
                                   val idData:Int,
                                   val cod:Int,
                                   val idProcess: Int,
                                   val idMaquina:Int,
                                   val peso: Float,
                                   val dataF: LocalDate?,
                                   val horaE: LocalTime,
                                   val horaS: LocalTime?,
                                   val nomeOp: String,
                                   val nomeProces:String,
                                   val nomeTipoRoupa: String,
                                   val numeroDaMAquina:String )
data class Processo(val id: Int,val nome: String,val descricao: String)

sealed class Funcoes{
    data class Operador(val id: Int,val descricao: String,val nome: String): Funcoes()
    data class Gerente(val id: Int,val descricao: String,val nome: String):Funcoes()
    data class Alsiliar(val id: Int,val descricao: String,val nome: String):Funcoes()
}