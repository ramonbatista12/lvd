package org.example.project.componentesComponiveis.Paineis.componentesRegistroDeMaquinas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.DropdownMenu
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import lanvanderia.composeapp.generated.resources.Res
import lanvanderia.composeapp.generated.resources.add_circle_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import lanvanderia.composeapp.generated.resources.close_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import lanvanderia.composeapp.generated.resources.delete_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import lanvanderia.composeapp.generated.resources.filter_list_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import lanvanderia.composeapp.generated.resources.search_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import org.example.project.repositorio.EntidadeDataRegistro
import org.example.project.repositorio.RespostaDeContagemDeMaquinas
import org.example.project.viewModel.ViewModelRegistroDeMaquinas
import org.jetbrains.compose.resources.painterResource
import java.time.LocalDate
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ApresentacaoListaDeRegistros(modifier: Modifier = Modifier,
                                 telasInterna: MutableState<PaginasDoPainel>,
                                 largura: Dp,
                                 vm: ViewModelRegistroDeMaquinas){

    val  dropdownMenuState =remember{ mutableStateOf(false) }
    val fluxoDeDatas =vm.fluxoDeDatasDeRegistro.collectAsState(emptyList())
    val filtragen =remember { mutableStateOf("") }
    val  opicoesDeFiltragen =remember { mutableStateOf(false) }
    val pesquisa =remember { mutableStateOf("") }
    val  pesquisaAberta =remember { mutableStateOf(false) }


    Column(modifier=modifier.fillMaxSize().padding(bottom = 20.dp, start = 20.dp, end = 20.dp)) {

        SearchBar(modifier = Modifier.border(width = 1.dp,androidx.compose.material3.MaterialTheme.colorScheme.onSurface, shape = RoundedCornerShape(5.dp))
                                     .heightIn(min = 50.dp)
                                     .width( 900.dp)
                                     .padding(end = 20.dp)
                                     .align(Alignment.CenterHorizontally),
            inputField = {
                SearchBarDefaults.InputField(query = pesquisa.value,
                    onQueryChange = {pesquisa.value=it},
                    onSearch = {},
                    onExpandedChange = {pesquisaAberta.value=it},
                    expanded = pesquisaAberta.value,
                    leadingIcon = {
                        Icon(painter = painterResource(Res.drawable.search_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                            contentDescription = "",
                            modifier = Modifier.clickable(onClick = { pesquisaAberta.value=false}) )},
                    trailingIcon = {
                        Icon(painter = painterResource(Res.drawable.close_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                            contentDescription = "",
                            modifier = Modifier.clickable(onClick = { pesquisaAberta.value=false}) )
                    } )
            },
            expanded = pesquisaAberta.value,
            onExpandedChange = {pesquisaAberta.value=it},
            colors = SearchBarDefaults.colors(containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,),
            shape = RoundedCornerShape(5.dp)){}

      Box(modifier= Modifier.align(Alignment.CenterHorizontally)){
          Row(Modifier.align(Alignment.TopStart).width(900.dp).padding(bottom =  50.dp, top = 20.dp), verticalAlignment = Alignment.CenterVertically){
              ExposedDropdownMenuBox(expanded = opicoesDeFiltragen.value,{opicoesDeFiltragen.value=!opicoesDeFiltragen.value}){
               OutlinedTextField(value = filtragen.value,{filtragen.value=it}, label = {Text("Opcoes de filtro de lista")})
              DropdownMenu(expanded = opicoesDeFiltragen.value, onDismissRequest = {opicoesDeFiltragen.value=!opicoesDeFiltragen.value}){
                  Row(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 3.dp)) {
                      Text(" Por Data", Modifier.clickable(onClick = {}))
                  }


                  Row(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 3.dp)) {
                      Text("Quantidae de maquinas", Modifier.clickable(onClick = {}))}

                  Row(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 3.dp)) {
                      Text("Por mais novos", Modifier.clickable(onClick = {}))}
                  Row(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 3.dp)) {
                      Text("Por mais antigos", Modifier.clickable(onClick = {}))}

              }}

              androidx.compose.material.Button(onClick = {}, Modifier.padding(start = 10.dp, end = 10.dp)){

                  Text(" Novo Registro")
              }



          }

          Spacer(Modifier.padding(20.dp))

      }






        AnimatedVisibility(visible = largura>1000.dp,modifier= Modifier.align(Alignment.CenterHorizontally)){
        LazyColumn(Modifier.border(width = 1.dp,
            color = Color.LightGray,
            shape = RoundedCornerShape(10.dp))
            .clip(shape = RoundedCornerShape(10.dp)).align (Alignment.CenterHorizontally))
        {
            stickyHeader {
                CabesalhoRegistros()
            }
            items(items= fluxoDeDatas.value){
                val visivel =remember { mutableStateOf(true) }
                if(it!=null)

                ItemRegistro(onClick = {
                    System.out.println()
                    vm.idData.value=it.id.value
                    telasInterna.value= PaginasDoPainel.VisualizacaoDoRegidtro(0)
                }, acaoDeContagem = {vm.contagemDeMaquiinasPorIdDaData(it.id.value)},
                    data = it, acaoDeApagarUmRegistro = {
                        vm.apagarUmRegistroDeDatas(it,
                            calbackDeEscluasao = {
                                visivel.value=!it

                            })
                    }, acaoChecagemConclusaoDasMaquinas = {vm.contarQuantidadeDeMaquinasAtivas(it).toInt()})

            }
        }}
        AnimatedVisibility(visible = largura<1000.dp,modifier= Modifier.align(Alignment.CenterHorizontally)){
            LazyColumn(Modifier){
                items(items= fluxoDeDatas.value){

                    val visivel =remember { mutableStateOf(true) }
                    if(it!=null)
                 ItemRegistroCompat(acao = {
                       vm.idData.value= it.id.value
                       telasInterna.value= PaginasDoPainel.VisualizacaoDoRegidtro(0)
                   }, acaoDeContagem = {vm.contagemDeMaquiinasPorIdDaData(it.id.value)},
                       data = it,
                       acaoDeApagarUmRegistro = {
                           vm.apagarUmRegistroDeDatas(it,{

                               visivel.value=!it
                           })

                       },
                     acoaDeContagemDeMaquinasativas = { vm.contarQuantidadeDeMaquinasAtivas(it).toInt()})}

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

                }
            }

        }
    }
}

@Composable
fun ItemRegistro(onClick: () -> Unit,
                 acaoDeContagem:suspend () -> RespostaDeContagemDeMaquinas,
                 data: EntidadeDataRegistro,
                 acaoDeApagarUmRegistro: (Int) -> Unit,
                 acaoChecagemConclusaoDasMaquinas:suspend (Int)->Int){
    val contagem =remember { mutableStateOf<RespostaDeContagemDeMaquinas>(RespostaDeContagemDeMaquinas.Load) }
    val maquinasConcluidas =remember { mutableStateOf(true) }
    LaunchedEffect(Unit){
        contagem.value =acaoDeContagem()
    }
    LaunchedEffect(Unit){
        maquinasConcluidas.value =if (acaoChecagemConclusaoDasMaquinas(data.id.value) >0) false else true
    }
    Column (Modifier.width(900.dp).clickable(onClick=onClick)) {
        HorizontalDivider()
        Spacer(Modifier.padding(3.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(all = 10.dp)) {


            Row(Modifier.width(150.dp)) {

                Text("${data.id}")

            }

            Row(Modifier.width(150.dp)) {

                Text("${data.data}")
            }
            Spacer(Modifier.padding(3.dp))
            Row(Modifier.width(150.dp)) {
                Text("${contagem.value.toString()}")}
            Spacer(Modifier.padding(3.dp))
            EstadoRegistro(comcluidas = maquinasConcluidas.value)
            OutlinedIconButton({acaoDeApagarUmRegistro(data.id.value)}){
                Icon(painter = painterResource(Res.drawable.delete_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                    contentDescription = "apagar registro",
                    Modifier.size(20.dp))
            }

        }
    }


}

@Composable
fun ItemRegistroCompat(acao:()-> Unit,
                       acaoDeContagem: suspend () -> RespostaDeContagemDeMaquinas,
                       data: EntidadeDataRegistro,
                       acaoDeApagarUmRegistro:(Int)-> Unit,
                       acoaDeContagemDeMaquinasativas:suspend (Int)->Int){
    val maquinasConcluidas = remember { mutableStateOf(false) }
    val contagenDeMaquinas = remember { mutableStateOf<RespostaDeContagemDeMaquinas>(RespostaDeContagemDeMaquinas.Load) }
    LaunchedEffect(Unit){
       contagenDeMaquinas.value= acaoDeContagem()
    }
    LaunchedEffect(Unit){
        maquinasConcluidas.value = if(acoaDeContagemDeMaquinasativas(data.id.value)>=0)false else true
    }
    OutlinedCard(modifier = Modifier.width(600.dp)
                                    .height(190.dp)
                                    .padding(10.dp).clickable(onClick = acao)) {
        Box(modifier = Modifier.fillMaxSize()){
            Box(Modifier.align(Alignment.TopCenter)
                        .fillMaxWidth().height(30.dp)
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 5.dp))
                        .background(color = if(maquinasConcluidas.value)Color.Red else Color.Green)){

                       if(maquinasConcluidas.value) Text(text = "Existem maquinas pendentes ", color = Color.White,
                                            modifier = Modifier.align(Alignment.Center))
                       else Text(text = "Todas as maquinaas foram concluidas", color = Color.White,
                                 modifier = Modifier.align(Alignment.Center)  )

            }
            Column(Modifier.align(Alignment.CenterStart).padding(start = 5.dp)) {
                Row {
                    Text("Id :")
                    Spacer(Modifier.padding(3.dp))
                    Text("${data.id}")
                    Spacer(Modifier.padding(end = 30.dp))}
                Spacer(Modifier.padding(5.dp))
                Row {
                    Text(text = "Data :")
                    Spacer(modifier = Modifier.padding( 3.dp))
                    Text("${data.data}")
                }
                Spacer(Modifier.padding(5.dp))
                Row {
                    Text("quantidade :")
                    Spacer(Modifier.padding(3.dp))
                    Text("${contagenDeMaquinas.value.toString()}")

                }
            }
            Row(Modifier.align(Alignment.BottomEnd).padding(end = 5.dp, bottom = 10.dp)) {
                OutlinedIconButton({acaoDeApagarUmRegistro(data.id.value)}){
                    Icon(painter = painterResource(Res.drawable.delete_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),""
                         , modifier = Modifier.size(20.dp))
                }
            }


        }
    }
}
@Composable
fun EstadoRegistro(comcluidas: Boolean){
    val estado = remember { mutableStateOf(Random.nextInt(1,5)) }
    val color = if(!comcluidas) Color.Red
    else Color.Green
    val text =if(!comcluidas)"Existem maquinas pendentes "
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