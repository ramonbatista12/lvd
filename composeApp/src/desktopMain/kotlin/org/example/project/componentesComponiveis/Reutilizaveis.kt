package org.example.project.componentesComponiveis

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import lanvanderia.composeapp.generated.resources.IconeDeRetorno
import org.jetbrains.compose.resources.painterResource
import lanvanderia.composeapp.generated.resources.Res


@Composable
fun IconButtonRetorno(acaoDeVoltar:()-> Unit, color: Color=Color.Black){
    IconButton(onClick = {acaoDeVoltar()}){
        Icon(painter =painterResource(resource = Res.drawable.IconeDeRetorno),
             contentDescription = "",
             modifier = Modifier.size(40.dp))
    }
}

@Composable
fun IconButtonRetorno(modifier: Modifier= Modifier, acaoDeVoltar:()-> Unit, color: Color=Color.Black){
    IconButton(onClick = {acaoDeVoltar()}, modifier = modifier){
        Icon(painter =painterResource(resource = Res.drawable.IconeDeRetorno),
            contentDescription = "",
            modifier = Modifier.size(40.dp))
    }
}

