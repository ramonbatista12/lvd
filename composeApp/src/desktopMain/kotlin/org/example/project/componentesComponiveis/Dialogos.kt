package org.example.project.componentesComponiveis

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.text.input.then
import androidx.compose.material.OutlinedSecureTextField
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lanvanderia.composeapp.generated.resources.Res
import org.example.project.DestinosDeNavegacao
import org.example.project.repositorio.Login

import org.example.project.viewModel.ViewModelPrincipal
import org.example.project.viewModel.ViewModelRegistroDeMaquinas
import java.time.LocalDate

@Composable
fun ApresentacoaDoDialogoDeLogin(viewModelPrincipal: ViewModelPrincipal,acaoDeVoutar:()-> Unit){
    val aberto = remember { mutableStateOf(true) }
    val textfildState = rememberTextFieldState()
    if(aberto.value){
        Dialog(onDismissRequest = {acaoDeVoutar()}){
            Surface{
                val arrLogin=arrayOf(remember { mutableStateOf("") },remember { mutableStateOf("") })
                OutlinedCard(modifier = Modifier.size(width = 350.dp, height = 250.dp)) {
                    Box(Modifier.fillMaxSize().padding(start = 20.dp, end = 20.dp)){
                        Text(text = "Login", modifier = Modifier.align(Alignment.TopCenter))
                        Column(Modifier.align(alignment = Alignment.Center)) {

                            Row(verticalAlignment = Alignment.CenterVertically) {
                               Text(text = "usuario")
                                Spacer(modifier = Modifier.padding(5.dp))
                                OutlinedTextField(value = arrLogin[0].value, onValueChange = {
                                    arrLogin[0].value=it
                                }, modifier = Modifier.height(50.dp).width(250.dp))
                            }

                            Spacer(Modifier.padding(10.dp))

                            Row(verticalAlignment = Alignment.CenterVertically){
                                Text(text = "senha")
                                Spacer(modifier = Modifier.padding(10.dp))
                                OutlinedSecureTextField(state = textfildState ,
                                    modifier = Modifier.height(50.dp).width(250.dp))
                            }
                        }
                        Row(Modifier.align(Alignment.BottomCenter)) {
                            TextButton(onClick = { acaoDeVoutar() }) {
                                Text(text = "Camcelar", color = Color.Red)
                            }
                            TextButton(onClick = {
                                viewModelPrincipal.logar(Login.Operador)
                            }) {
                                Text(text = "Confirmar", color = Color.Blue)
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun ApresentacaoDialogoCriarNovaData(boolean: MutableState<Boolean>,
                                     vm: ViewModelRegistroDeMaquinas,
                                     acaoIrParaProsimaTela:()-> Unit={}){
    if(boolean.value)
    Dialog(onDismissRequest = {boolean.value=false}){
    val scope = rememberCoroutineScope()
    val textFildState =rememberTextFieldState()
    LaunchedEffect(Unit){
        val dataAtual = LocalDate.now().toString()
        System.out.println("data atual ${dataAtual}")
        val split =dataAtual.split(Regex("-"))
        textFildState.setTextAndPlaceCursorAtEnd("${split[2]}${split[1]}${split[0]}")
    }
    OutlinedCard(Modifier.width(300.dp)
                         .height(230.dp)) {
        Box(Modifier.fillMaxSize().padding(10.dp)){
             Text(text = "Data de referencia", Modifier.align(Alignment.TopCenter))

         Row(Modifier.align(Alignment.Center)){
             OutlinedTextField(state = textFildState,
                               label = {Text("Data")},
                               modifier = Modifier.width(280.dp),
                               inputTransformation = InputTransformation.maxLength(FormatadoresDeTexto.tamanhoData)
                                   .then(FormatadoresDeTexto.InputTransformationData()),
                               outputTransformation = FormatadoresDeTexto.OutputTrasnformatinData()
                               )

         }
            Button(onClick = {
                scope.launch(Dispatchers.Default) {
                  val id=  vm.criarEAdicinarDataNoBancoDeDados(textFildState.text.toString())
                  vm.idData.emit(id)
                  acaoIrParaProsimaTela()

                }
            },
                   modifier = Modifier.align(Alignment.BottomStart)){
                Text("Confirmar")
            }
            Button(onClick = {boolean.value=false},
                modifier = Modifier.align(Alignment.BottomEnd)){
                Text("Cancelar")
            }
        }

    }
    }
}