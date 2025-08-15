package org.example.project

import androidx.annotation.Dimension
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowDecorationDefaults
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import lanvanderia.composeapp.generated.resources.Fechar
import lanvanderia.composeapp.generated.resources.Maximisar
import lanvanderia.composeapp.generated.resources.MinimizarPagina
import org.example.project.viewModel.ViewModelPrincipal
import java.awt.Color
import lanvanderia.composeapp.generated.resources.Res
import org.example.project.repositorio.Conecao
import org.example.project.repositorio.Login


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun main() = application {
    val  windowState =rememberWindowState( size = DpSize(1080.dp,700.dp))
    val scop = rememberCoroutineScope()
    scop.launch {

        Conecao.selectUsers()
        Conecao.getUserById(1)
        val logim: Login = Conecao.usuariobyName("0003","123456789")
        System.out.println("login obtido ${logim}")
    }


    Window(
        state = windowState,
        onCloseRequest = ::exitApplication,
        title = "Lanvanderia",
        undecorated = false,




    ) {
//        with(LocalDensity.current){
 //           val size= DpSize(1080.dp,700.dp)
//            window.minimumSize= java.awt.Dimension(size.width.value.toInt(),size.height.value.toInt())
//        }
        val windowSizeClas = calculateWindowSizeClass()
        LaunchedEffect(windowSizeClas.widthSizeClass){
            System.out.println("$windowSizeClas")
        }


       val viewModel: ViewModelPrincipal = viewModel()

            App(viewModel)
        }


}