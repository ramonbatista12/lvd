package org.example.project.componentesComponiveis.Paineis.componentesEstoque

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.flow.MutableStateFlow
import org.example.project.componentesComponiveis.Paineis.componentesDemanutencao.Etapa
import org.example.project.componentesComponiveis.Paineis.componentesSolicitacoesEpis.EstadosSolicitacaoDeIpis

@Composable
fun  rememberEstadoSolicitacaoEpi()= rememberSaveable(saver = EtadoEtapasSolicitacoesEstoque.saver()){
    EtadoEtapasSolicitacoesEstoque(EtapasSolicitacaoEstoque.ColetaDadosDoFuncionario)

}

@Composable
fun  rememberEstadoSolicitacaoEpi(inicial: EtapasSolicitacaoEstoque)= rememberSaveable(saver = EtadoEtapasSolicitacoesEstoque.saver()){
    EtadoEtapasSolicitacoesEstoque(inicial)

}


class EtadoEtapasSolicitacoesEstoque(private val inicial: EtapasSolicitacaoEstoque){
    val estado= MutableStateFlow<EtapasSolicitacaoEstoque>(inicial)
    suspend fun proximo(){
        val  proximo =estado.value.proximo()
        if(proximo!=null){
            estado.value= proximo as EtapasSolicitacaoEstoque
            return
        }
    }
    suspend fun anterios(){
        val anterior =estado.value.anterior()
        if(anterior!=null){
            estado.value =anterior as EtapasSolicitacaoEstoque
        }
    }
    fun indice(): String =estado.value.indice
    companion object{
        fun saver()=object : Saver<EtadoEtapasSolicitacoesEstoque, String>{
            override fun SaverScope.save(value: EtadoEtapasSolicitacoesEstoque): String? {
               return  value.indice()
            }

            override fun restore(value: String): EtadoEtapasSolicitacoesEstoque? {
               when(value){
                   "0"->return EtadoEtapasSolicitacoesEstoque(EtapasSolicitacaoEstoque.ColetaDadosDoFuncionario)
                   "1"->return EtadoEtapasSolicitacoesEstoque(EtapasSolicitacaoEstoque.SelecaoDeProdutosNoEstoque)
                   else -> throw RuntimeException("valor salvo em saver nao comdis com os indices de EtapasSolicitacoesEpis")
               }
            }

        }
    }
}


sealed class EtapasSolicitacaoEstoque(val indice: String): Etapa{
    object ColetaDadosDoFuncionario: EtapasSolicitacaoEstoque("0"){
        override fun anterior(): Etapa?=null
        override fun proximo(): Etapa? = SelecaoDeProdutosNoEstoque}
    object SelecaoDeProdutosNoEstoque: EtapasSolicitacaoEstoque("1"){
        override fun anterior(): Etapa? = ColetaDadosDoFuncionario
         override fun proximo(): Etapa?=null}
}