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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import lanvanderia.composeapp.generated.resources.Res
import lanvanderia.composeapp.generated.resources.close_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import lanvanderia.composeapp.generated.resources.delete_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import lanvanderia.composeapp.generated.resources.filter_list_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import lanvanderia.composeapp.generated.resources.search_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import org.example.project.componentesComponiveis.ApresentacaoDialogoCriarNovaData
import org.example.project.componentesComponiveis.EntradaDeTextoBaraPesquiPorFiltro
import org.example.project.componentesComponiveis.Paineis.componentesRegistroDeMaquinas.EstadoFiltroPesquisa.Data
import org.example.project.componentesComponiveis.Paineis.componentesRegistroDeMaquinas.EstadoFiltroPesquisa.Id
import org.example.project.componentesComponiveis.Paineis.componentesRegistroDeMaquinas.EstadoFiltroPesquisa.QuantidadeDeMaquinas
import org.example.project.repositorio.DataDeRegistro
import org.example.project.repositorio.EntidadeDataRegistro
import org.example.project.repositorio.RespostaDeContagemDeMaquinas
import org.example.project.viewModel.DataRegistro
import org.example.project.viewModel.ViewModelRegistroDeMaquinas
import org.jetbrains.compose.resources.painterResource


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
    val pesquisa =rememberTextFieldState("")
    val  pesquisaAberta =remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope ()
    val criarNovaData =remember { mutableStateOf(false) }

    Column(modifier=modifier.fillMaxSize().padding(bottom = 20.dp, start = 20.dp, end = 20.dp)) {

    BaraDePesquisa(modifier= Modifier.align(Alignment.CenterHorizontally), vm = vm)

      Box(modifier= Modifier.align(Alignment.CenterHorizontally)){
          Row(Modifier.align(Alignment.TopStart).width(900.dp).padding(bottom =  5.dp, top = 20.dp), verticalAlignment = Alignment.CenterVertically){
             /* ExposedDropdownMenuBox(expanded = opicoesDeFiltragen.value,{opicoesDeFiltragen.value=!opicoesDeFiltragen.value}){
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

              }}*/

              androidx.compose.material.Button(onClick = {
                  criarNovaData.value=true
              }, Modifier.padding(start = 0.dp, end = 10.dp)){

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
                    vm.idData.value=it.id
                    telasInterna.value= PaginasDoPainel.VisualizacaoDoRegidtro(0)
                }, acaoDeContagem = {vm.contagemDeMaquiinasPorIdDaData(it.id)},
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
                       vm.idData.value= it.id
                       telasInterna.value= PaginasDoPainel.VisualizacaoDoRegidtro(0)
                   }, acaoDeContagem = {vm.contagemDeMaquiinasPorIdDaData(it.id)},
                       data = it,
                       acaoDeApagarUmRegistro = {
                           vm.apagarUmRegistroDeDatas(it,{

                               visivel.value=!it
                           })

                       },
                     acoaDeContagemDeMaquinasativas = { vm.contarQuantidadeDeMaquinasAtivas(it).toInt()})}

            }
            }

        ApresentacaoDialogoCriarNovaData(criarNovaData,vm,{telasInterna.value= PaginasDoPainel.VisualizacaoDoRegidtro(0)})
        }
    }




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaraDePesquisa(modifier: Modifier= Modifier,vm: ViewModelRegistroDeMaquinas){
    val  opicoesDeFiltragen =remember { mutableStateOf(false) }

    val  pesquisaAberta =remember { mutableStateOf(false) }
    val filtroDePesquisaSelelcionado=rememberFiltroSelelcionado()
    val coroutineScope = rememberCoroutineScope ()
    val pesquisa =rememberTextFieldState("")
    val stadoFiltro =rememberFiltroSelelcionado()
    SearchBar(modifier = modifier.border(width = 1.dp,androidx.compose.material3.MaterialTheme.colorScheme.onSurface, shape = RoundedCornerShape(5.dp))
        .heightIn(min = 50.dp)
        .width( 900.dp)
        .padding(end = 20.dp, start = 20.dp),

        inputField = {EntradaDeTextoBaraPesquiPorFiltro( filtroDePesquisaSelelcionado=filtroDePesquisaSelelcionado,
                                                         textFildState = pesquisa,
                                                         expandido = pesquisaAberta.value,
                                                         acaoDeExpandir = { pesquisaAberta.value=it },
                                                         iconePesquisa = {Icon(modifier = it, painter = painterResource(
                                                             Res.drawable.search_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24), contentDescription = "")},
                                                         iconeDeFiltro = {Icon(modifier = it,painter = painterResource(
                                                             Res.drawable.filter_list_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24), contentDescription = "")},
                                                         iconeDeClose = {it,calbac->
                                                             Icon(modifier = it.clickable(onClick = {
                                                                pesquisa.clearText()
                                                                 pesquisaAberta.value=false

                                                             }),painter = painterResource(Res.drawable.close_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                                                                 contentDescription = "",
                                                             )} ,
                                                         acaoDePesquisa = {
                                                             System.out.println("acao de pesquisa acontecendo $it")
                                                         }    )


             },
        expanded = pesquisaAberta.value,
        onExpandedChange = {pesquisaAberta.value=it},
        colors = SearchBarDefaults.colors(containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,),
        shape = RoundedCornerShape(5.dp)){

         }
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
                 data: DataDeRegistro,
                 acaoDeApagarUmRegistro: (Int) -> Unit,
                 acaoChecagemConclusaoDasMaquinas:suspend (Int)->Int){
    val contagem =remember { mutableStateOf<RespostaDeContagemDeMaquinas>(RespostaDeContagemDeMaquinas.Load) }
    val maquinasPendentes =remember { mutableStateOf(true) }


    LaunchedEffect(data){
        contagem.value =acaoDeContagem()
    }
    LaunchedEffect(data.id){
        maquinasPendentes.value = (acaoChecagemConclusaoDasMaquinas(data.id) >0)
    }
    Column (Modifier.width(900.dp).clickable(onClick=onClick)) {
        HorizontalDivider()
        Spacer(Modifier.padding(3.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(all = 10.dp)) {


            Row(Modifier.width(150.dp)) {

                Text("${data.id}")

            }

            Row(Modifier.width(150.dp)) {

                Text("${data.dataRegistro.dataFormatada()}")
            }
            Spacer(Modifier.padding(3.dp))
            Row(Modifier.width(150.dp)) {
                Text("${contagem.value.toString()}")}
            Spacer(Modifier.padding(3.dp))
            EstadoRegistro(pendentes = maquinasPendentes.value)
            OutlinedIconButton({acaoDeApagarUmRegistro(data.id)}){
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
                       data: DataDeRegistro,
                       acaoDeApagarUmRegistro:(Int)-> Unit,
                       acoaDeContagemDeMaquinasativas:suspend (Int)->Int){
    val maquinasConcluidas = remember { mutableStateOf(false) }
    val contagenDeMaquinas = remember { mutableStateOf<RespostaDeContagemDeMaquinas>(RespostaDeContagemDeMaquinas.Load) }
    LaunchedEffect(data.id){
       contagenDeMaquinas.value= acaoDeContagem()
    }
    LaunchedEffect(data.id){
        val contagem =acoaDeContagemDeMaquinasativas(data.id)
        maquinasConcluidas.value = if(contagem>=0)false else true
        System.out.println("contagen para id =${data.id} cont:${contagem} , maquinas comcluidas ${maquinasConcluidas.value}")
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
                    Text("${data.dataRegistro.dataFormatada()}")
                }
                Spacer(Modifier.padding(5.dp))
                Row {
                    Text("quantidade :")
                    Spacer(Modifier.padding(3.dp))
                    Text("${contagenDeMaquinas.value.toString()}")

                }
            }
            Row(Modifier.align(Alignment.BottomEnd).padding(end = 5.dp, bottom = 10.dp)) {
                OutlinedIconButton({acaoDeApagarUmRegistro(data.id)}){
                    Icon(painter = painterResource(Res.drawable.delete_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),""
                         , modifier = Modifier.size(20.dp))
                }
            }


        }
    }
}
@Composable
fun EstadoRegistro(pendentes: Boolean){

    val color = if(pendentes) Color.Red
    else Color.Green
    val text =if(pendentes)"Existem maquinas pendentes "
    else "Todas as maquinas foram comcluidas"
    Row(modifier = Modifier.width(290.dp)) {
        Box(modifier = Modifier.clip(CircleShape).size(20.dp).background(color=color))
        Spacer(Modifier.padding(3.dp))
        Text(text = text)
    }
}

@Composable
fun rememberFiltroSelelcionado(inicial: EstadoFiltroPesquisa)= rememberSaveable(FiltroDePesquisaSelelcionado.saver){
    FiltroDePesquisaSelelcionado(inicial)
}
@Composable
fun rememberFiltroSelelcionado()= rememberSaveable(FiltroDePesquisaSelelcionado.saver){
    FiltroDePesquisaSelelcionado(EstadoFiltroPesquisa.Data)
}
class FiltroDePesquisaSelelcionado(val inicial : EstadoFiltroPesquisa){
    val estado= MutableStateFlow<EstadoFiltroPesquisa>(inicial)
    fun setStado(stado: EstadoFiltroPesquisa){
        estado.value= estado as EstadoFiltroPesquisa
    }
    companion object{
        val saver =object : Saver<FiltroDePesquisaSelelcionado, String>{
            override fun restore(value: String): FiltroDePesquisaSelelcionado? {
                return when(value){
                    "1" -> FiltroDePesquisaSelelcionado( Data)
                    "2" -> FiltroDePesquisaSelelcionado( QuantidadeDeMaquinas)
                    "3" -> FiltroDePesquisaSelelcionado(Id)
                    else -> throw IllegalStateException("valor salvo nao representa nenum dos indices originais")
                }
            }

            override fun SaverScope.save(value: FiltroDePesquisaSelelcionado): String? {
                return value.estado.value.indice
            }
        }
    }
}


sealed class EstadoFiltroPesquisa(val indice:String){
    object Data: EstadoFiltroPesquisa("1")
    object QuantidadeDeMaquinas: EstadoFiltroPesquisa("2")
    object Id: EstadoFiltroPesquisa("3")

}



/*
*

* */