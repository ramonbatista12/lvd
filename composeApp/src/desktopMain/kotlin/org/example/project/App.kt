package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.mouseClickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.WindowState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import lanvanderia.composeapp.generated.resources.Res
import lanvanderia.composeapp.generated.resources.compose_multiplatform
import org.example.project.componentesComponiveis.BarraLateralPermanente
import org.example.project.repositorio.Login
import org.example.project.repositorio.Repositorio

import org.example.project.viewModel.ViewModelPrincipal

@Composable
@Preview
fun App( viewModelPrincipal: ViewModelPrincipal,repositorio: Repositorio) {
    val estadoLogin=viewModelPrincipal.fluxoDeLogin.collectAsState()
    MaterialTheme {

        Surface {
            val  navHostController = rememberNavController()
            val  scop= rememberCoroutineScope ()
           // val sizeclass = currentWindowAdaptiveInfo().windowSizeClass
            val destino =remember { mutableStateOf<DestinosDeNavegacao>(DestinosDeNavegacao.Home) }
            val estadoDeLogin =viewModelPrincipal.fluxoDeLogin.collectAsState()
            val colorBackgraumd = Color(0x00A8FF)




               PermanentNavigationDrawer(modifier = Modifier.fillMaxSize(),
                   drawerContent = {
                       BarraLateralPermanente(acaoDeNavegacao = {
                       navHostController.navigate(it)},
                                              acaoDeLogin = {navHostController.navigate(DestinosDeNavegacao.DestinosDeDialogo.DialogoDeLogin)},
                                              acaoLogOf = {
                                                  scop.launch {
                                                  viewModelPrincipal.desconectar()
                                                  navHostController.navigate(DestinosDeNavegacao.DestinosDeDialogo.DialogoDeLogin)
                                              }
                                              }, estadoDoLogin = estadoDeLogin)
                        },
                   ){
                   Box(modifier = Modifier.fillMaxSize(1f)){
                       Navigraf(navHost = navHostController,
                                modifier = Modifier.fillMaxSize(),
                                estado=estadoLogin,
                               viewModelPrincipal =  viewModelPrincipal,
                               repositorio= repositorio
                                )
                   }
               }

            LaunchedEffect(Unit){
                scop.launch {

                    if(viewModelPrincipal.fluxoDeLogin.value== Login.Desconectado)
                  navHostController.navigate(DestinosDeNavegacao.DestinosDeDialogo.DialogoDeLogin)

                        System.out.println("o estado de login e : ${estadoLogin.value.toString()}")
                }
            }

        }
        }


}

suspend fun  mostratComposicoesNavigrafi(destino: MutableState<DestinosDeNavegacao>): DestinosDeNavegacao{
    when(destino.value){
        is DestinosDeNavegacao.Home->return DestinosDeNavegacao.Gerenciamento
        is DestinosDeNavegacao.Gerenciamento -> return DestinosDeNavegacao.Requisicoes
        is DestinosDeNavegacao.RegistroDeMaquinas -> return DestinosDeNavegacao.Solicitacoes
        is DestinosDeNavegacao.Requisicoes -> return DestinosDeNavegacao.RegistroDeMaquinas
        else ->{}
    }
    return DestinosDeNavegacao.Home
}