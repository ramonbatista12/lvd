package org.example.project.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.example.project.repositorio.EntidadeRegistroDeMaquinas
import org.example.project.repositorio.RegistroDeMaquinasPorId
import org.example.project.repositorio.Repositorio
import org.example.project.repositorio.RespostaDeContagemDeMaquinas
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass

class ViewModelRegistroDeMaquinas(private val repositorio: Repositorio): ViewModel(){
 private val vmScope = viewModelScope
  val idData =MutableStateFlow(0)
 private val fluxoDeMaquinsPorDatas= idData.flatMapLatest {
     if(it==0) flowOf( emptyList<RegistroDeMaquinasPorId>())
     else fluxoDeregistroDeDados(it)
 }.stateIn(vmScope, started = SharingStarted.WhileSubscribed(500),emptyList())
 val _fluxoDeRegistroDeDatas =fluxoDeMaquinsPorDatas

 val fluxoDeMaquinas=repositorio.fluxoDeMaquinas().stateIn(vmScope, SharingStarted.WhileSubscribed(500),emptyList())
 val fluxoDeProcessos=repositorio.fluxoDeProcessos().stateIn(vmScope, SharingStarted.WhileSubscribed(500),emptyList())
 val fluxoTiposDeRoupas=repositorio.fluxoTipoDeRoupas().stateIn(vmScope, SharingStarted.WhileSubscribed(500),emptyList())
 val fluxoDeDatasDeRegistro =repositorio.fluxoDeDatasDeRegistro().stateIn(vmScope, SharingStarted.WhileSubscribed(500),emptyList())


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

    suspend fun maecarRegistroComoFinalizado(idRegistroDeMaquinas: Int){
        vmScope.launch {
            System.out.println("id do registro chamado no view model para definir como finalizado")
              repositorio.definirRegistroComoFinalizado(idRegistroDeMaquinas, LocalDate.now(), LocalTime.now())
        }
    }

    fun fluxoDeregistroDeDados(idData: Int) =   repositorio.fluxoDeregistroPorDatas(idData)


    suspend fun apagarRegistroDeMaquina(idRegistroDeMaquinas: Int)=repositorio.apagarRegistroDeMaquinaPeloId(idRegistroDeMaquinas)

    suspend fun criarData(dataNaoFormatada: String): LocalDate {
        val formatacao = DateTimeFormatter.ofPattern("ddMMyyyy")
        val localDate = LocalDate.parse(dataNaoFormatada,formatacao)
        System.out.println("data feita no viewmodel ${localDate.toString()}")
     return localDate
    }
    suspend fun adicionarDataAoBancoDeDados(localDate: LocalDate) =vmScope.async { repositorio.criarNovaData(localDate) }.await()
    suspend fun criarEAdicinarDataNoBancoDeDados(dataNaoFormatada: String): Int{
           val localDate=  criarData(dataNaoFormatada)
           val  entidade =adicionarDataAoBancoDeDados(localDate)
        System.out.println("ultimo oid adicionado na tabela datas de registro ${entidade.id_data.value}")

    return entidade.id_data.value
    }
    suspend fun adicionarRegistroAoBancoDeDados(peso:String,idMaquina:Int,idDoTipo:Int,idProcesso:Int,codigoOperador:Int){

         repositorio.adicionarRegistroDeMaquinasAoBAncoDeDados(idData=idData.value,
                                                               peso = peso.toFloat(),
                                                               idProcesso = idProcesso,
                                                               idTipo = idDoTipo,
                                                               codOpera = codigoOperador,
                                                               idMaquina = idMaquina,
                                                               hora = LocalTime.now())
    }
    suspend fun editarRegistro(idData:Int,peso:String,idMaquina:Int,idDoTipo:Int,idProcesso:Int,codigoHoperador:Int,dataSaida:String?,horaFinalizacao: String?){

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