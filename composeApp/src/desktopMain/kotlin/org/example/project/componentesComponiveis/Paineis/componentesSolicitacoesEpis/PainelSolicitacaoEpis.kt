package org.example.project.componentesComponiveis.Paineis.componentesSolicitacoesEpis

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@Composable
fun PainelSolicitacaoEpi(modifier: Modifier= Modifier){
    val etapasEpis =remenberEstadosSolicitacaoDeIpis()
    val etapasVisiveis = Array(2,{ remember { mutableStateOf(false) } })
    val etapa =etapasEpis.estado.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Column (modifier=modifier, horizontalAlignment = Alignment.CenterHorizontally){


    when(etapa.value){
        is EtapasSolicitacoesEpis.ObterDados -> {
            System.out.println("mudanca de etapa foi acionada etapa :obter dados")
            LaunchedEffect(Unit){
                etapasVisiveis[0].value =true
                etapasVisiveis[1].value =false

            }
            AnimatedVisibility(visible = etapasVisiveis[0].value){
                ColetaDeDadosDoUsuarioParaSolicitarEpi(acacoProximo = {
                    coroutineScope.launch {
                        System.out.println("corotine escope acionado")
                        etapasEpis.proximo()
                    }
                })
            }
        }
        is EtapasSolicitacoesEpis.ListarPedidos -> {
            LaunchedEffect(Unit){
                System.out.println("mudanca de etapa foi acionada etapa : listar pedidos")
                etapasVisiveis[0].value =false
                etapasVisiveis[1].value =true

            }
            AnimatedVisibility(visible = etapasVisiveis[1].value){
              ListarPedidoIpi(acaoAnterior = {
                  coroutineScope.launch { etapasEpis.anterior() }})
            }

        }
    }

    }

}