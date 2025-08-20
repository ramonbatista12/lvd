package org.example.project.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.example.project.repositorio.Repositorio
import org.example.project.repositorio.RespostaDeContagemDeMaquinas
import kotlin.reflect.KClass

class ViewModelRegistroDeMaquinas(private val repositorio: Repositorio): ViewModel(){
 private val vmScope = viewModelScope
 val fluxoDeDatasDeRegistro =repositorio.fluxoDeDatasDeRegistro()

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

    suspend fun contarQuantidadeDeMaquinasAtivas(idData: Int) =repositorio.quantidadeDeMaquianssAtivas(idData)

    fun fluxoDeregistroDeDados(idData: Int) = repositorio.fluxoDeregistroPorDatas(idData)



}


class FabricaViewModelRegistroDeMaquinas{
    fun fabricar(r: Repositorio) =object : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
            return ViewModelRegistroDeMaquinas(r) as T
        }
    }
}