package org.example.project.componentesComponiveis.Paineis.componentesRegistroDeMaquinas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import lanvanderia.composeapp.generated.resources.Res
import lanvanderia.composeapp.generated.resources.chronic_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import lanvanderia.composeapp.generated.resources.delete_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import lanvanderia.composeapp.generated.resources.edit_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import org.example.project.componentesComponiveis.IconButtonRetorno
import org.example.project.repositorio.EntidadeRegistroDeMaquinas
import org.example.project.repositorio.EntidadeTabelaDeMaquinas
import org.example.project.repositorio.EntidadeTabelaProcesso
import org.example.project.repositorio.EntidadeTipoDeRoupas
import org.example.project.viewModel.ViewModelRegistroDeMaquinas
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.random.Random


@Composable
 fun ApresentacaoDoRegistroDeMAquinas(modifier: Modifier= Modifier, larguraDaTela: Dp, telasDoPainel: MutableState<PaginasDoPainel>, vm: ViewModelRegistroDeMaquinas){
    val  editando = remember { mutableStateOf(false) }
    val fluxoDeMaquinas = vm._fluxoDeRegistroDeDatas.collectAsState()
    val fluxoListaMaquinas =vm.fluxoDeMaquinas.collectAsState()
    val porcessos=vm.fluxoDeProcessos.collectAsState()
    val tiposdeRoupas =vm.fluxoTiposDeRoupas.collectAsState()
    val coroutineScope =rememberCoroutineScope()


    Column(modifier= modifier.fillMaxWidth().fillMaxHeight().padding(start = 30.dp, top = 4.dp)) {
        IconButtonRetorno({
            telasDoPainel.value= PaginasDoPainel.ListaDeRegistros
        })
        Spacer(modifier.padding(4.dp))
        ColetarDados(Modifier.padding(start = 4.dp),
                     editando,
                    listaDeMaquinas = fluxoListaMaquinas.value,
                    processos = porcessos.value,tiposdeRoupas.value)
        Row (modifier = Modifier.padding(bottom = 5.dp, start = 5.dp)){
            //  RegistroDeMaquinas(Modifier.padding(10.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ListaDeRegistros(Modifier.fillMaxHeight().fillMaxWidth(0.95f).padding(bottom = 10.dp),
                                 100,
                                 larguraDaTela,
                                 editando,
                                fluxoDeMaquinas.value,
                                 coroutineScope,
                    {},
                    {vm.apagarRegistroDeMaquina(it)})
            }
        }



    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ColetarDados(modifier: Modifier= Modifier,
                 editando: MutableState<Boolean>,
                 listaDeMaquinas: List<EntidadeTabelaDeMaquinas>,
                 processos:List<EntidadeTabelaProcesso>,
                 tipoDeRoupas: List<EntidadeTipoDeRoupas>){
    val estadoTextiFildPeso=rememberTextFieldState()
    val estadoTextiFildProcesso=rememberTextFieldState()
    val estadoTextiFildCodigoOperador=rememberTextFieldState()
    val estadoTextiFildMAquina=rememberTextFieldState()
    val estadoTipoDeroupa=rememberTextFieldState()
    val focoTipoDeroupa=remember{ MutableInteractionSource() }
    val opcoesTipoDeroupaEstaAberta=remember { mutableStateOf(false) }
    val opcoesDeMAquinas=remember { mutableStateOf(false) }
    val opcoesDeProcesso=remember { mutableStateOf(false) }
    Column(modifier=modifier) {
        FlowRow(){
            ExposedDropdownMenuBox(expanded = opcoesDeMAquinas.value, onExpandedChange = {
                opcoesDeMAquinas.value=!opcoesDeMAquinas.value
            }){
                OutlinedTextField(state = estadoTextiFildMAquina, label = {Text("Maquina")})
                ExposedDropdownMenu(expanded = opcoesDeMAquinas.value, onDismissRequest = {opcoesDeMAquinas.value=!opcoesDeMAquinas.value}){
                    listaDeMaquinas.forEach {
                        Row(Modifier.fillMaxWidth()) {
                            Text("Maquina numero ${it.numeroDaMaquina}    ")
                            Text("Peso  ${it.pesoMinimo}...${it.pesoMasimo}")
                        }
                    }
                }
            }

            Spacer(Modifier.padding(10.dp))
            OutlinedTextField(state = estadoTextiFildPeso, label = {Text("Digite o Peso")})
            Spacer(Modifier.padding(10.dp))
            ExposedDropdownMenuBox(expanded = opcoesDeProcesso.value,{opcoesDeProcesso.value=!opcoesDeProcesso.value}){
                OutlinedTextField(state = estadoTextiFildProcesso, label = {Text("Digite o Processo")})
                ExposedDropdownMenu(expanded = opcoesDeProcesso.value,{opcoesDeProcesso.value=!opcoesDeProcesso.value}){
                   processos.forEach {
                    Row(modifier = Modifier.fillMaxWidth().clickable(onClick = {})) {
                        Text("process:${it.nome} ")
                        Text(" numero:${it.numero}")

                    }
                }
                }
            }

            Spacer(Modifier.padding(10.dp))
            OutlinedTextField(state = estadoTextiFildCodigoOperador, label = {Text("Digite o Codigo operador")})
            Spacer(Modifier.padding(10.dp))

            ExposedDropdownMenuBox(expanded = opcoesTipoDeroupaEstaAberta.value,
                                   onExpandedChange = {opcoesTipoDeroupaEstaAberta.value=!opcoesTipoDeroupaEstaAberta.value}){
                OutlinedTextField(state = estadoTextiFildCodigoOperador, label = {Text("Tipo De Roupa")})
                ExposedDropdownMenu(expanded = opcoesTipoDeroupaEstaAberta.value,{opcoesTipoDeroupaEstaAberta.value=!opcoesTipoDeroupaEstaAberta.value}){
                     tipoDeRoupas.forEach {
                      Row(modifier = Modifier.fillMaxWidth().clickable(onClick = {})) {
                        Text("${it.nome}")

                    }
                     }

            }}
            Spacer(Modifier.padding(10.dp))
            AnimatedVisibility(visible = editando.value){

                    OutlinedTextField(state = estadoTextiFildCodigoOperador, label = {Text("Digite a Hora de entrada")})
            }
            Spacer(Modifier.padding(10.dp))
            AnimatedVisibility(visible = editando.value){

                    OutlinedTextField(state = estadoTextiFildCodigoOperador, label = {Text("Digite a Hora de saida")})
        }




        }
        Button({
            if(editando.value) editando.value=false
        }){

                AnimatedVisibility(visible = editando.value){Text("Salvar edicao")}
            AnimatedVisibility(visible = !editando.value) {Text("Adicionar Registro ")}

        }
    }
}



@Composable
fun OpcoesPainelRegistroDeMaquinas(
    modifier: Modifier,
    acaoDeVoltar: () -> Unit,

    ) {
    val scop =rememberCoroutineScope()
    val clicado= remember { mutableStateOf<OpcoesDeRegistroDeMAquinas?>(null) }
    FlowRow (modifier) {
        IconButtonRetorno(acaoDeVoltar=acaoDeVoltar)
        OpcoesDeRegistroDeMAquinas.Companion.listaDeOpcoes.forEach {
            Spacer(Modifier.padding(3.dp))
            OutlinedButton(onClick = {
                scop.launch {
                    clicado.value=it
                }
            }){
                Text("${it?.titulo}")
            }
        }
    }
}

@Composable
fun ListaDeRegistros(modifier: Modifier= Modifier,
                     cont: Int,larguraDaTela: Dp,
                     editando: MutableState<Boolean>,
                     listaDeMaquinas: List<EntidadeRegistroDeMaquinas>,
                     coroutineScope: CoroutineScope,
                     acaoDeMarcarLavagemComoFinalizada:suspend (Int) -> Unit,
                     acoaoDeApagarRegistro:suspend (Int)-> Unit){
   AnimatedVisibility(visible = larguraDaTela>1015.dp){
    LazyColumn(modifier.border(width = if(larguraDaTela>1015.dp) 1.dp else 0.dp, color = Color.LightGray, shape = RoundedCornerShape(5.dp))) {
        stickyHeader {

            CabesalhoDaListaDeMaquinas(larguraDaTela)
        }
        items(items = listaDeMaquinas){
         if(it!=null)
        ItemDaListaDeMaquinas(6,larguraDaTela,editando,it,coroutineScope,{
            coroutineScope.launch {
                acaoDeMarcarLavagemComoFinalizada(it)
            }
        },
            {coroutineScope.launch { acoaoDeApagarRegistro(it) }}  )
        }

    }}
    AnimatedVisibility(visible = larguraDaTela<1015.dp){


    LazyColumn(modifier) {
        items(listaDeMaquinas){
            ItemDaListaCompacto(editando,it,coroutineScope)
        }
    }
    }
}

@Composable
fun ItemDaListaDeMaquinas(numerosDeCampos:Int,largura: Dp,
                          editando: MutableState<Boolean>,
                          e: EntidadeRegistroDeMaquinas,
                          coroutineScope: CoroutineScope,
                          acaoDeMarcarLavagemComoFinalizada:(Int)-> Unit,
                          acaoDeApagarRegistro:(Int)->Unit) {
    val finalizado =remember { mutableStateOf(false) }
    val numeroAleatoria = Random.nextInt(-1,1)
    val dados = Array(5,{remember { mutableStateOf("") }})
    val lavando =remember { mutableStateOf(false) }
    LaunchedEffect(Unit){
        coroutineScope.launch {
            dados[0].value=async(Dispatchers.IO) {transaction {  e.processo.nome}  }.await()
            dados[1].value=async(Dispatchers.IO){ transaction{e.tipo.nome } }.await()
            dados[2].value=async(Dispatchers.IO) { transaction{e.operador.nome}  }.await()
            dados[3].value =async(Dispatchers.IO) { transaction { e.maquinaUsada.numeroDaMaquina.toString() } }.await()

        }
    }
    LaunchedEffect(Unit){
        coroutineScope.launch {
            lavando.value=async(Dispatchers.IO) {transaction { e.finalizada }  }.await()
        }
    }
    Column(modifier = Modifier.padding(top = 20.dp)) {
        Spacer(Modifier.padding(10.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.padding(3.dp))
        Row(modifier = Modifier.padding(start = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(dados[3].value, modifier = Modifier.width(150.dp))
            Spacer(Modifier.padding(3.dp))
            Text(dados[0].value, modifier = Modifier.widthIn(150.dp))
            Spacer(Modifier.padding(3.dp))
            AnimatedVisibility(largura>=1330.dp){
                Text("${e.peso}", modifier = Modifier.widthIn(150.dp))
                Spacer(Modifier.padding(3.dp))}
            Text("${e.entrada}", modifier = Modifier.widthIn(150.dp))
            Spacer(Modifier.padding(3.dp))
            AnimatedVisibility(visible = largura>1439.dp){
                Text(dados[1].value, modifier = Modifier.width(150.dp), maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.padding(3.dp))
            }

            AnimatedVisibility(largura>=1330.dp){
                Text(dados[2].value, modifier = Modifier.widthIn(150.dp))
                Spacer(Modifier.padding(3.dp))
            }
            EstadoDamaquna(lavando.value)
            Spacer(Modifier.padding(3.dp))
            FlowRow(){
                AnimatedVisibility(visible = !lavando.value){
                    OutlinedButton({
                          acaoDeMarcarLavagemComoFinalizada(e.id.value)
                    }){
                        Icon(
                            painterResource(Res.drawable.chronic_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24)
                            ,"Exclui", modifier = Modifier.size(20.dp))
                    }}
                Spacer(Modifier.padding(3.dp))
                OutlinedButton({
                    editando.value=true
                }){
                    Icon(
                        painterResource(Res.drawable.edit_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24)
                        ,"editar", modifier = Modifier.size(20.dp))
                }

                Spacer(Modifier.padding(3.dp))
                OutlinedButton({acaoDeApagarRegistro(e.id.value)}){
                    Icon(
                        painterResource(Res.drawable.delete_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24)
                        ,"Exclui", modifier = Modifier.size(20.dp))
                }

            }


        }
    }
}

@Composable
fun ItemDaListaCompacto(editando: MutableState<Boolean>,
                        e: EntidadeRegistroDeMaquinas,
                        coroutineScope: CoroutineScope
                        ) {
    val finalizado = remember { mutableStateOf(false) }
    val numeroAleatoria = Random.nextInt(-1, 1)
    val dados = Array(size = 5,{remember { mutableStateOf("") }})
    LaunchedEffect(Unit){
        finalizado.value=async { transaction { e.finalizada } }.await()
    }
    LaunchedEffect(Unit){
        coroutineScope.launch {
            dados[0].value=async(Dispatchers.IO) {transaction {  e.processo.nome}  }.await()
            dados[1].value=async(Dispatchers.IO){ transaction{e.tipo.nome } }.await()
            dados[2].value=async(Dispatchers.IO) { transaction{e.operador.nome}  }.await()
            dados[3].value =async(Dispatchers.IO) { transaction { e.maquinaUsada.numeroDaMaquina.toString() } }.await()}
    }
    OutlinedCard(modifier = Modifier.padding(top = 20.dp).height(190.dp)) {
        Box(Modifier.fillMaxSize()) {
            Box(
                Modifier.fillMaxWidth().height(40.dp)
                    .background(color = if (finalizado.value) Color.Green else Color.Red)
                    .clip(RoundedCornerShape(topStart = 10.dp, bottomEnd = 10.dp))
            ) {
             Row(modifier = Modifier.align(Alignment.Center)) {
                 AnimatedVisibility(
                     visible = !finalizado.value,) {
                     Text(text = "Lavando", color = Color.White)
                 }
                 AnimatedVisibility(
                         visible = finalizado.value,

                 ) {
                 Text(text = "Finalizado as ${e.saida}", color = Color.White)
             }
             }




            }

            Column(Modifier.align(Alignment.CenterStart).padding(start = 10.dp)) {
                Row {
                    Text("Maquina",)
                    Spacer(Modifier.padding(end = 30.dp))
                    Text(dados[3].value, )
                    Spacer(Modifier.padding(end = 30.dp))
                    Text("Processo :")
                    Spacer(Modifier.padding(end = 3.dp))
                     Text(dados[0].value, modifier = Modifier)
                    Spacer(Modifier.padding(end = 30.dp))
                    Text("Peso :")
                    Spacer(Modifier.padding(end = 3.dp))
                    Text(e.peso.toString(), modifier = Modifier)
                    Spacer(Modifier.padding(end = 30.dp))
                    Text("Roupa :")
                    Spacer(Modifier.padding(end = 3.dp))
                    Text(dados[1].value, modifier = Modifier)


                }
                Spacer(Modifier.padding(top = 10.dp))
                Row {
                    Text("Hora entrada :")
                    Spacer(Modifier.padding(end = 3.dp))
                    Text(e.entrada.toString(), )
                    Spacer(modifier = Modifier.padding(end = 30.dp))
                    Text("Operador :")
                    Spacer(Modifier.padding(end = 30.dp))
                    Text(dados[2].value,)
                }


            }

            Row(modifier = Modifier.fillMaxWidth().align(Alignment.BottomEnd).padding(bottom = 5.dp, end = 5.dp), horizontalArrangement = Arrangement.End) {
                AnimatedVisibility(visible = !finalizado.value) {
                    OutlinedButton({
                        finalizado.value = !finalizado.value
                    }) {
                        Icon(
                            painterResource(Res.drawable.chronic_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                            "Exclui",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(Modifier.padding(3.dp))
                OutlinedButton({
                    editando.value = true
                }) {
                    Icon(
                        painterResource(Res.drawable.edit_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                        "editar",
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.padding(3.dp))
                OutlinedButton({}) {
                    Icon(
                        painterResource(Res.drawable.delete_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                        "Exclui",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

        }


    }
}



@Composable
fun EstadoDamaquna(boolean: Boolean){
    AnimatedVisibility(visible = !boolean)
    {

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.widthIn(150.dp)) {
            Box(modifier = Modifier.size(20.dp).clip(CircleShape).background(Color.Red))
            Spacer(Modifier.padding(3.dp))
            Text("Lavando")
        }
    }
    AnimatedVisibility(boolean){
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.widthIn(150.dp)) {
            Box(modifier = Modifier.size(20.dp).clip(CircleShape).background(Color.Green))
            Spacer(Modifier.padding(3.dp))
            Text("Finalizado as 11:00")
        }
    }

}

@Composable
fun CabesalhoDaListaDeMaquinas(maxwith: Dp) {

    val listaDeCabesalho1 =  listOf<String>("Maquina","Processo","Peso","Hr inicio","Cod operador","Estado")
    val listaDeCabesalho2= listOf<String>("Maquina","Processo","Hr inicio","Estado")
    val listaDeCabesalho3= listOf<String>("Maquina","Processo","Peso","Hr inicio","Cod operador","Estado")

    OutlinedCard(shape = RoundedCornerShape(size = 2.dp), modifier = Modifier.padding(bottom = 10.dp)) {


        Row(modifier = Modifier.padding(start = 10.dp, bottom = 10.dp).height(70.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            ItemDeCabesalho("Maquina", Modifier.widthIn(150.dp))
            Spacer(modifier = Modifier.padding(3.dp))
            ItemDeCabesalho("Processo", Modifier.widthIn(150.dp))
            Spacer(modifier = Modifier.padding(3.dp))
            AnimatedVisibility(visible = maxwith>=1330.dp) {
                ItemDeCabesalho("Peso", Modifier.widthIn(150.dp))
                Spacer(modifier = Modifier.padding(3.dp))
            }
            ItemDeCabesalho("Hr inicio", Modifier.widthIn(150.dp))
            Spacer(modifier = Modifier.padding(3.dp))
            AnimatedVisibility(visible = maxwith>1439.dp){
                ItemDeCabesalho("Roupa", Modifier.widthIn(150.dp))
            }

            Spacer(modifier = Modifier.padding(3.dp))
            AnimatedVisibility(visible = maxwith>=1330.dp)  {
                ItemDeCabesalho("Operador", Modifier.widthIn(150.dp))
                Spacer(modifier = Modifier.padding(3.dp))
            }
            ItemDeCabesalho("Estado", Modifier.widthIn(150.dp))
            Spacer(modifier = Modifier.padding(3.dp))

        }
    }
}

@Composable
fun ItemDeCabesalho(titulo: String,modifier: Modifier= Modifier){
    Text(text = titulo, color = Color.Black, fontSize = 20.sp,modifier=modifier)
}