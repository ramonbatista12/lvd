package org.example.project.componentesComponiveis.Paineis.componentesRegistroDeMaquinas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp

@Composable
fun PainelRegistroDeMAquinas(modifier: Modifier= Modifier,
                             acaoDeVoltar:()-> Unit={}){
    val  coroutineScope = rememberCoroutineScope()
    val scroll = rememberScrollState(0)
    val telasInternas =remember { mutableStateOf<PaginasDoPainel>(PaginasDoPainel.ListaDeRegistros) }
    val listaderegistrosEstaVisivel=remember { mutableStateOf(true) }
    val  apresentacaoPorRegistroEsAberta =remember { mutableStateOf(false) }

    BoxWithConstraints(modifier= modifier) {
    LaunchedEffect(maxWidth){
      System.out.println("largura maxima  $maxWidth")
    }
    val larguraDaTela  = remember(maxWidth) {mutableStateOf(maxWidth) }
    LaunchedEffect(larguraDaTela.value){
        System.out.println("largura registrada ${larguraDaTela.value}")
    }
     //OpcoesPainelRegistroDeMaquinas(modifier= Modifier.align(Alignment.TopStart), acaoDeVoltar = acaoDeVoltar)
      when(telasInternas.value){
          is PaginasDoPainel.ListaDeRegistros ->{
              DisposableEffect(Unit){
                  listaderegistrosEstaVisivel.value=true
                  apresentacaoPorRegistroEsAberta.value=false

                  onDispose {
                      listaderegistrosEstaVisivel.value=false

                      apresentacaoPorRegistroEsAberta.value=true
                  }
              }
              AnimatedVisibility(visible = listaderegistrosEstaVisivel.value ,modifier= Modifier.align(Alignment.CenterStart).fillMaxWidth()){
                  ApresentacaoListaDeRegistros(
                      modifier = Modifier.align(Alignment.CenterStart).fillMaxWidth()
                          .padding(start = 30.dp, top = 3.dp), telasInternas, largura = maxWidth
                  )
              }

          }
          is PaginasDoPainel.VisualizacaoDoRegidtro-> {
              DisposableEffect(Unit){
                  listaderegistrosEstaVisivel.value=false

                  apresentacaoPorRegistroEsAberta.value=true

                  onDispose {
                      apresentacaoPorRegistroEsAberta.value=false
                      listaderegistrosEstaVisivel.value=true

                  }
              }
              AnimatedVisibility(visible = apresentacaoPorRegistroEsAberta.value){
                  ApresentacaoDoRegistroDeMAquinas(
                      modifier = Modifier.align(Alignment.CenterStart).fillMaxWidth()
                          .padding(start = 30.dp, top = 3.dp), larguraDaTela = maxWidth, telasInternas
                  )
              }

          }


      }



    }
}


sealed class OpcoesDeRegistroDeMAquinas(val titulo: String){
    object Registro: OpcoesDeRegistroDeMAquinas("Registro de maquinas")
    object Pesquisa: OpcoesDeRegistroDeMAquinas("Pesquisa registro")
    companion object{
        val listaDeOpcoes=listOf<OpcoesDeRegistroDeMAquinas>(
            Registro,
            Pesquisa
        )
    }
}

sealed class PaginasDoPainel{
    data class VisualizacaoDoRegidtro(val idDoRegistros: Int): PaginasDoPainel(){
        fun voltar(): PaginasDoPainel = ListaDeRegistros


    }
    object ListaDeRegistros: PaginasDoPainel()

}