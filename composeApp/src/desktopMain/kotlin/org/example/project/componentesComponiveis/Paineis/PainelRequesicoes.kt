package org.example.project.componentesComponiveis.Paineis

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.componentesComponiveis.IconButtonRetorno

@Composable
fun PainelRequesicao(modifier: Modifier= Modifier,acaoDeVoltar:()-> Unit){
    BoxWithConstraints (modifier=modifier){
            OpcoesDeSolicitacoes(Modifier.fillMaxWidth(),acaoDeVoltar=acaoDeVoltar)
    }
}

@Composable
fun OpcoesDeSolicitacoes(modifier: Modifier= Modifier,acaoDeVoltar: () -> Unit){
    FlowRow(modifier = modifier){
        IconButtonRetorno(acaoDeVoltar = acaoDeVoltar)
        Spacer(Modifier.padding(3.dp))
        OutlinedButton(onClick = {}){
            Text(text = "Registra Solicitacao")
        }
        Spacer(Modifier.padding(3.dp))
        OutlinedButton(onClick = {}){
            Text(text = "Atendidas")
        }
        Spacer(Modifier.padding(3.dp))
        OutlinedButton(onClick = {}){
            Text(text = "Nao tendidas")
        }
        Spacer(Modifier.padding(3.dp))
        OutlinedButton(onClick = {}){
            Text(text = "Observacoe")
        }
    }
}