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
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.OutlinedSecureTextField
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import lanvanderia.composeapp.generated.resources.Res
import org.example.project.DestinosDeNavegacao
import org.example.project.repositorio.Login

import org.example.project.viewModel.ViewModelPrincipal

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