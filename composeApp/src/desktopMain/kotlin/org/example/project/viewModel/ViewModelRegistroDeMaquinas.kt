package org.example.project.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.example.project.repositorio.Repositorio
import org.example.project.repositorio.RespostaDeContagemDeMaquinas
import java.util.Date
import java.util.Timer
import kotlin.reflect.KClass

class ViewModelRegistroDeMaquinas(private val repositorio: Repositorio): ViewModel(){
 private val vmScope = viewModelScope
 val fluxoDeDatasDeRegistro =repositorio.fluxoDeDatasDeRegistro()
 private val idData= MutableStateFlow<Int>(0)

   suspend fun contagemDeMaquiinasPorIdDaData(id:Int): RespostaDeContagemDeMaquinas{
        try {
            val contagem =vmScope.async { repositorio.contagemDeMaquinasPorIdDaData(id) }.await()
            return RespostaDeContagemDeMaquinas.Contagem(quantidae = contagem)
        }catch (e: Exception){
          return RespostaDeContagemDeMaquinas.Erro(e.message.toString())
        }
   }

    fun apagarUmRegistroDeDatas(idData:Int,calbackDeEscluasao:(Boolean)-> Unit) {
        vmScope.launch {
            try {
                val apagado = repositorio.apagarUmRegistroDeDatas(idData)
                calbackDeEscluasao(apagado)

            } catch (e: Exception) {
                System.out.println("erro ${e.message}")
                calbackDeEscluasao(false)
                e.printStackTrace()
            }

        }
    }

    suspend fun contarQuantidadeDeMaquinasAtivas(idData: Int) =vmScope.async {   repositorio.quantidadeDeMaquianssAtivas(idData)}.await()

    suspend fun maecarRegistroComoFinalizado(idRegistroDeMaquinas: Int,dataFinalizacao: String,horaFinalizacao: String){
        vmScope.launch {

        }
    }

    fun fluxoDeregistroDeDados(idData: Int) =   repositorio.fluxoDeregistroPorDatas(idData)

    suspend fun checarData(data: String){
        val regex ="^(0[1-9]|2[0-9]|3[0-1])/(0[1-9]|1[0-2])/\\d{4}"
        if(!data.contains(regex)) throw RuntimeException("formato de data nao aseito $data  formato aseito ->$regex ")
    }
    suspend fun checarHora(data: String){
        val regex ="^([0-1][0-9]|2[0-3]):[0-5][0-9]"
        if(!data.contains(regex)) throw RuntimeException("formato de data nao aseito $data  formato aseito ->$regex ")
    }

}


class FabricaViewModelRegistroDeMaquinas{
    fun fabricar(r: Repositorio) =object : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
            return ViewModelRegistroDeMaquinas(r) as T
        }
    }
}

class DataRegistro(id: Int,data: String,estado: Boolean)