package org.example.project.componentesComponiveis.Paineis

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.unpackInt1
import org.example.project.componentesComponiveis.IconButtonRetorno

import org.jetbrains.skia.Region

@Composable
fun PainelDeGerencia(modifier: Modifier,
                     acaoDeVoultar:()-> Unit,
                    ){
    BoxWithConstraints(modifier = modifier) {
      OpcoesPainelDeGerenciamento(modifier = Modifier.align(Alignment.TopStart),acaoDeVoultar=acaoDeVoultar )
    }
}

@Composable
fun OpcoesPainelDeGerenciamento(modifier: Modifier= Modifier,
                                acaoDeVoultar: () -> Unit,
                                ){
    val opcaoClicada= remember { mutableStateOf<Opcoes?>(null) }
    LaunchedEffect(Unit){
        opcaoClicada.value= Opcoes.RelatorioDasMaquinas
    }
    FlowRow(modifier =modifier ){
        IconButtonRetorno(acaoDeVoltar = acaoDeVoultar)
        Opcoes.list.forEach {it->
            Spacer(modifier = Modifier.padding(3.dp))
            BotaoDeOpcoes(it,opcaoClicada)
        }



    }
}

@Composable fun BotaoDeOpcoes(opcao: Opcoes?,opcaoClicada: MutableState<Opcoes?>){
    System.out.println("opcao $opcao")
    OutlinedButton(onClick ={
        opcaoClicada.value=opcao
    }){
        Text("${opcao?.titulo}")
    }
}
sealed class Opcoes(val titulo:String){
    object RelatorioDasMaquinas: Opcoes(titulo ="Relatorio das maquinas")
object RelatorioDaColeta: Opcoes(titulo = "Relatorio da coleta")
object GerenciamentoDeProdutos: Opcoes( titulo = "Gerenciamento de produtos")
object RelatorioDeRequesicoes: Opcoes("Relatorio de requesicoes")
companion object{
    val list = listOf<Opcoes>(RelatorioDasMaquinas,RelatorioDaColeta, GerenciamentoDeProdutos,
        RelatorioDeRequesicoes)
}
}