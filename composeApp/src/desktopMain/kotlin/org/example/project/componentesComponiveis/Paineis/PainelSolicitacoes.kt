package org.example.project.componentesComponiveis.Paineis

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.componentesComponiveis.Paineis.componentesDemanutencao.ItemsOpcoesSolicitacoes
import org.example.project.componentesComponiveis.Paineis.componentesDemanutencao.OpcoesSolicitacoes
import org.example.project.componentesComponiveis.Paineis.componentesDemanutencao.PainelSoliciTarManutencao


import org.example.project.componentesComponiveis.Paineis.componentesDemanutencao.rememberOpcoesSolicitacao
import org.example.project.componentesComponiveis.Paineis.componentesEstoque.PainelEstoque
import org.example.project.componentesComponiveis.Paineis.componentesSolicitacoesEpis.PainelSolicitacaoEpi

@Composable
fun PainelSolicitacoes (modifier: Modifier= Modifier,acaoDeVoultar:()-> Unit){
    val clicado = rememberOpcoesSolicitacao()
    val estadosVisualizacao = Array(size = 3,{ remember { mutableStateOf(false) } })
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        ItemsOpcoesSolicitacoes(acaoDeRetorno = acaoDeVoultar,
                                clicado = clicado,
                                modifier = Modifier.align(Alignment.TopStart))
        Column(Modifier.fillMaxWidth().fillMaxHeight().align(Alignment.Center).padding(top = 60.dp)) {
            Box(modifier = Modifier.fillMaxWidth()){
                when(clicado.value){
                    is OpcoesSolicitacoes.Epi -> {
                        PainelSolicitacaoEpi(modifier = modifier)
                    }
                    is OpcoesSolicitacoes.Estoque -> {
                        PainelEstoque(m = modifier)
                    }
                    is OpcoesSolicitacoes.Manutencao -> {


                        PainelSoliciTarManutencao(modifier)

                    }
                    else -> {}
                }

            }

        }



    }
}