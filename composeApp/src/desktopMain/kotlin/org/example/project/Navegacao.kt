package org.example.project

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import kotlinx.coroutines.launch
import org.example.project.componentesComponiveis.ApresentacoaDoDialogoDeLogin
import org.example.project.componentesComponiveis.Paineis.PainelDeGerencia
import org.example.project.componentesComponiveis.Paineis.componentesRegistroDeMaquinas.PainelRegistroDeMAquinas
import org.example.project.componentesComponiveis.Paineis.PainelRequesicao
import org.example.project.componentesComponiveis.Paineis.PainelSolicitacoes
import org.example.project.repositorio.Login
import org.example.project.repositorio.Repositorio
import org.example.project.viewModel.FabricaViewModelRegistroDeMaquinas

import org.example.project.viewModel.ViewModelPrincipal

@Composable
fun Navigraf(navHost: NavHostController,
             modifier: Modifier=Modifier,
             estado: State<Login>,
             viewModelPrincipal: ViewModelPrincipal,
             repositorio: Repositorio){
    val coroutine = rememberCoroutineScope ()
    NavHost(navController = navHost, startDestination = DestinosDeNavegacao.RegistroDeMaquinas,modifier=modifier){
        composable<DestinosDeNavegacao.Requisicoes>{
            PainelRequesicao(modifier = modifier.fillMaxSize(), acaoDeVoltar = {
                coroutine.launch {
                    navHost.popBackStack()
                }
            })
        }
        composable<DestinosDeNavegacao.Solicitacoes> {
            PainelSolicitacoes(modifier.fillMaxSize(), acaoDeVoultar = {
                coroutine.launch {
                    navHost.popBackStack()
                }
            })
        }
        composable<DestinosDeNavegacao.RegistroDeMaquinas> {
            PainelRegistroDeMAquinas(acaoDeVoltar = {
                coroutine.launch {
                    navHost.popBackStack()
                }
            }, modifier = modifier.fillMaxSize(),
            vm = viewModel(factory = FabricaViewModelRegistroDeMaquinas().fabricar(repositorio))
            )
        }

        composable<DestinosDeNavegacao.Gerenciamento> {
            PainelDeGerencia(modifier= Modifier, acaoDeVoultar = {
                coroutine.launch {
                    navHost.popBackStack()
                }
            })
        }

        composable<DestinosDeNavegacao.Home> {
           Box(modifier=modifier) {Text(text = "Home", modifier = Modifier.align(androidx.compose.ui.Alignment.Center))}

        }

        dialog<DestinosDeNavegacao.DestinosDeDialogo.DialogoDeLogin> {
            ApresentacoaDoDialogoDeLogin(viewModelPrincipal = viewModelPrincipal,{
                navHost.popBackStack()
            })
        }

    }
}