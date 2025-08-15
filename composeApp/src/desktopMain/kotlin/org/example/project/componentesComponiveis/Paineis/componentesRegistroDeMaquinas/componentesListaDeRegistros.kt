package org.example.project.componentesComponiveis.Paineis.componentesRegistroDeMaquinas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import lanvanderia.composeapp.generated.resources.Res
import lanvanderia.composeapp.generated.resources.add_circle_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import lanvanderia.composeapp.generated.resources.delete_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import lanvanderia.composeapp.generated.resources.filter_list_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import lanvanderia.composeapp.generated.resources.search_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import org.jetbrains.compose.resources.painterResource
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApresentacaoListaDeRegistros(modifier: Modifier = Modifier, telasInterna: MutableState<PaginasDoPainel>, largura: Dp){

    val  dropdownMenuState =remember{ mutableStateOf(false) }
    Column(modifier=modifier.fillMaxSize().padding(bottom = 20.dp, start = 20.dp)) {


        BaraDePesquisa()

        OutlinedButton(onClick = {},
            modifier =  Modifier.align(Alignment.CenterHorizontally),
        ){
            Icon(painter = painterResource(Res.drawable.add_circle_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                contentDescription = null,
                modifier = Modifier.size(30.dp),
            )
            Text(" Novo Registro")
        }
        Spacer(Modifier.padding(20.dp))






        AnimatedVisibility(visible = largura>1000.dp,modifier= Modifier.align(Alignment.CenterHorizontally)){
        LazyColumn(Modifier.border(width = 1.dp,
            color = Color.LightGray,
            shape = RoundedCornerShape(10.dp))
            .clip(shape = RoundedCornerShape(10.dp)).align (Alignment.CenterHorizontally))
        {
            stickyHeader {
                CabesalhoRegistros()
            }
            items(count = 20){
                ItemRegistro({
                    telasInterna.value= PaginasDoPainel.VisualizacaoDoRegidtro(0)
                })
            }
        }}
        AnimatedVisibility(visible = largura<1000.dp,modifier= Modifier.align(Alignment.CenterHorizontally)){
            LazyColumn(Modifier){
                items(count = 20){
                   ItemRegistroCompat({
                       telasInterna.value= PaginasDoPainel.VisualizacaoDoRegidtro(0)
                   })
                }
            }
        }
    }


}

@Composable
fun BaraDePesquisa(modifier: Modifier= Modifier){
    val estadoPesquisa =rememberTextFieldState()
    Box(modifier= Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.CenterEnd)
                .padding(end = 10.dp, bottom = 10.dp)){
            OutlinedTextField(state = estadoPesquisa, Modifier ,label =
                {Text("Pesquisa")
                }, shape = RoundedCornerShape(20.dp),
                leadingIcon = {Icon(painter = painterResource(Res.drawable.search_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                    contentDescription = "")},
                lineLimits = TextFieldLineLimits.SingleLine)


            Spacer(Modifier.padding(10.dp))

        }        }
}
@Composable
fun CabesalhoRegistros(){
    val dropdownMenuState=remember { mutableStateOf(false) }
    OutlinedCard(Modifier.width(900.dp), shape = RoundedCornerShape(2.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(all = 10.dp)) {


            Row(Modifier.width(150.dp)) {

                Text("Id ")

            }

            Row(Modifier.width(150.dp)) {

                Text("Data ")
            }
            Spacer(Modifier.padding(3.dp))
            Row(Modifier.width(150.dp)) {
                Text("Quantidade")}
            Spacer(Modifier.padding(3.dp))
            Row {
                Text("Estado ")
            }
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){


                Column() {
                    TextButton({dropdownMenuState.value=true}){
                        Text("filtrar")
                        androidx.compose.material3.Icon(painter = painterResource(Res.drawable.filter_list_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                            contentDescription = "filtrar lista",
                            modifier = Modifier.size(20.dp))
                    }
                    DropdownMenu(expanded = dropdownMenuState.value, onDismissRequest = {dropdownMenuState.value=false}){
                        Row(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 3.dp)) {
                            Text(" Por Data", Modifier.clickable(onClick = {}))
                        }


                        Row(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 3.dp)) {
                            Text("Quantidae de maquinas", Modifier.clickable(onClick = {}))}

                        Row(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 3.dp)) {
                            Text("Por mais novos", Modifier.clickable(onClick = {}))}
                        Row(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 3.dp)) {
                            Text("Por mais antigos", Modifier.clickable(onClick = {}))}

                    }
                }
            }

        }
    }
}

@Composable
fun ItemRegistro(onClick:()-> Unit){
    Column (Modifier.width(900.dp).clickable(onClick=onClick)) {
        HorizontalDivider()
        Spacer(Modifier.padding(3.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(all = 10.dp)) {


            Row(Modifier.width(150.dp)) {

                Text("100000")

            }

            Row(Modifier.width(150.dp)) {

                Text("01/01/2000")
            }
            Spacer(Modifier.padding(3.dp))
            Row(Modifier.width(150.dp)) {
                Text("50")}
            Spacer(Modifier.padding(3.dp))
            EstadoRegistro()
            OutlinedIconButton({}){
                Icon(painter = painterResource(Res.drawable.delete_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                    contentDescription = "apagar registro",
                    Modifier.size(20.dp))
            }

        }
    }


}

@Composable
fun ItemRegistroCompat(acao:()-> Unit){
    val aleatorio =remember { Random.nextInt(1,3) }
    OutlinedCard(modifier = Modifier.width(600.dp)
                                    .height(190.dp)
                                    .padding(10.dp).clickable(onClick = acao)) {
        Box(modifier = Modifier.fillMaxSize()){
            Box(Modifier.align(Alignment.TopCenter)
                        .fillMaxWidth().height(30.dp)
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 5.dp))
                        .background(color = if(aleatorio==1)Color.Red else Color.Green)){

                       if(aleatorio==1) Text(text = "Existem maquinas pendentes ", color = Color.White,
                                            modifier = Modifier.align(Alignment.Center))
                       else Text(text = "Todas as maquinaas foram concluidas", color = Color.White,
                                 modifier = Modifier.align(Alignment.Center)  )

            }
            Column(Modifier.align(Alignment.CenterStart).padding(start = 5.dp)) {
                Row {
                    Text("Id :")
                    Spacer(Modifier.padding(3.dp))
                    Text("100000")
                    Spacer(Modifier.padding(end = 30.dp))}
                Spacer(Modifier.padding(5.dp))
                Row {
                    Text(text = "Data :")
                    Spacer(modifier = Modifier.padding( 3.dp))
                    Text("10/10/2020")
                }
                Spacer(Modifier.padding(5.dp))
                Row {
                    Text("quantidade :")
                    Spacer(Modifier.padding(3.dp))
                    Text("50")

                }
            }
            Row(Modifier.align(Alignment.BottomEnd).padding(end = 5.dp, bottom = 10.dp)) {
                OutlinedIconButton({}){
                    Icon(painter = painterResource(Res.drawable.delete_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),""
                         , modifier = Modifier.size(20.dp))
                }
            }


        }
    }
}
@Composable
fun EstadoRegistro(){
    val estado = remember { mutableStateOf(Random.nextInt(1,5)) }
    val color = if(estado.value==1) Color.Red
    else Color.Green
    val text =if(estado.value==1) "Existem maquinas pendentes "
    else "Todas as maquinas foram comcluidas"
    Row(modifier = Modifier.width(290.dp)) {
        Box(modifier = Modifier.clip(CircleShape).size(20.dp).background(color=color))
        Spacer(Modifier.padding(3.dp))
        Text(text = text)
    }
}
/*
*

* */