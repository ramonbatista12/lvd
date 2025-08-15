package org.example.project.componentesComponiveis.Paineis.componentesDemanutencao

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.project.componentesComponiveis.IconButtonRetorno

@Composable
fun ItemsOpcoesSolicitacoes(modifier: Modifier= Modifier,
                            clicado: MutableState<OpcoesSolicitacoes?> = rememberOpcoesSolicitacao(),
                            acaoDeRetorno:()-> Unit){
    LaunchedEffect(Unit){
        clicado.value= OpcoesSolicitacoes.Manutencao
    }
    val scrollState = rememberScrollState(0)
    FlowRow(modifier=modifier.fillMaxWidth(1f)
                             .verticalScroll(scrollState)
                             ){
      IconButtonRetorno(acaoDeVoltar = acaoDeRetorno,)
        OpcoesSolicitacoes.list.forEach {
            Spacer(Modifier.padding(3.dp))
            ItemOpcoesSolicitacoes(it,clicado)
        }






    }
}




interface Etapa{
    fun anterior(): Etapa?
    fun proximo(): Etapa?
}

@Composable
fun rememberOpcoesSolicitacao()= remember { mutableStateOf<OpcoesSolicitacoes?>(null) }

sealed class OpcoesSolicitacoes(val titulo: String){
    object Manutencao: OpcoesSolicitacoes(titulo = "Manutencao ")

    object Estoque: OpcoesSolicitacoes(titulo = "Estoque")
    object Epi: OpcoesSolicitacoes(titulo = "Epi")
    companion object{
      val  list =listOf<OpcoesSolicitacoes>(Manutencao, Estoque, Epi)
    }
}

@Composable
fun ItemOpcoesSolicitacoes(objeto: OpcoesSolicitacoes?, clicado: MutableState<OpcoesSolicitacoes?>){
    OutlinedButton(onClick = {
     clicado.value =objeto

    }, border = BorderStroke(width = 1.dp,
                                                       color = if(objeto==clicado.value) Color.Magenta
                                                              else Color.LightGray)){
        Text(text =if (objeto==null) "null" else objeto!!.titulo,)
    }
}



