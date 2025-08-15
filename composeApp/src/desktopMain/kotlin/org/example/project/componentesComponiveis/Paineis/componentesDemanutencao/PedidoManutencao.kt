package org.example.project.componentesComponiveis.Paineis.componentesDemanutencao

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenu
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


@Composable
fun BarraDeProgressoManutencao(modifier: Modifier= Modifier,controtroleEtapas: ControtroleEtapasPedidoManutencao){

    val listaDeetapas =controtroleEtapas.fluxoListaDeEtapas.collectAsState()





    LazyRow (modifier= Modifier.width(800.dp)) {
        items(items = listaDeetapas.value){
            ItemDeProgressoDeEtapasPedidosDeManutencao("${it.titulo}",if(it.inicio)true else false ,if (it.fim)true else false, label = {
                label(titulo = it,modifier= Modifier.width(200.dp).height(40.dp))
        })
        }




        }
    }

@Composable
fun label(titulo: String,modifier: Modifier= Modifier){
    OutlinedCard(modifier, border = BorderStroke(width = 1.dp, color = Color.Magenta)){
        Box(modifier = Modifier.fillMaxSize()){
            Text(text = titulo, modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun ColetaDeDados(modifier: Modifier,etapa: MutableState<EtapasPedidoManutencao>,acaoRetornarEtapa: () -> Unit={},acaoAvancarEtapa: () -> Unit={}){
    val  dadosDoFuncionario =remember { mutableStateOf(true) }
    val  dadosDoPedido =remember { mutableStateOf(false) }
    val  verificarPedido =remember { mutableStateOf(false) }
   when(etapa.value){
       is EtapasPedidoManutencao.EtapaColetarDadosSobreFuncionario->{
           LaunchedEffect(Unit){
               dadosDoFuncionario.value=true
               dadosDoPedido.value=false
               verificarPedido.value=false
           }
           AnimatedVisibility(visible = dadosDoFuncionario.value,modifier=modifier){
               CardDadosDoFuncionario(acaoAvancarEtapa = acaoAvancarEtapa)
           }
       }
       is EtapasPedidoManutencao.EtapaObterDadosDoPedido -> {
           LaunchedEffect(Unit){
               dadosDoFuncionario.value=false
               dadosDoPedido.value=true
               verificarPedido.value=false
           }
           AnimatedVisibility(visible = dadosDoPedido.value,modifier){
                CardDadosDoPedido(acaoAvancarEtapa = acaoAvancarEtapa, acaoRetornarEtapa = acaoRetornarEtapa)
           }
       }
       is EtapasPedidoManutencao.EtapaVerificarPedido -> {
           LaunchedEffect(Unit){
               dadosDoFuncionario.value=false
               dadosDoPedido.value=false
               verificarPedido.value=true
           }
           AnimatedVisibility(visible = verificarPedido.value,modifier){
            CardVerificarPedido(acaoRetornarEtapa = acaoRetornarEtapa)
           }
       }
   }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardDadosDoFuncionario(modifier: Modifier= Modifier,acaoAvancarEtapa:()-> Unit={}){
    val nomeDoFuncionario =remember { mutableStateOf("") }
    val setor=remember { mutableStateOf("") }
    val codigo=remember { mutableStateOf("") }
    val focuSetor=remember{ MutableInteractionSource() }
    val maquina=remember { mutableStateOf("") }
    val focuMAquinas=remember{ MutableInteractionSource() }
    val opcoesDeSetores=remember{ mutableStateOf(false) }
    val opcoesDeManutencao=remember{ mutableStateOf(false) }
    val tipoManutencao=remember { mutableStateOf("") }
    val localidade =remember { mutableStateOf("") }
    val opicoesDeLocalidade=remember { mutableStateOf(false) }
    OutlinedCard(modifier=modifier.fillMaxWidth(0.7f)) {



        Box(modifier = Modifier.fillMaxSize().padding(10.dp)){
            FlowRow(Modifier.align(Alignment.TopStart).padding(20.dp)) {
                OutlinedTextField(value = nomeDoFuncionario.value,
                                  onValueChange = {nomeDoFuncionario.value=it},
                                  label = {Text("Nome do Requisitante")},
                                  modifier= Modifier.width(810.dp))
                MenuDeSetore(opcoesDeSetores,setor)
                MenuLocalidade(opicoesDeLocalidade,localidade)
                MenuTipoManutencao(opcoesDeManutencao,tipoManutencao)
                OutlinedTextField(value = codigo.value,
                        onValueChange = {codigo.value=it},
                        label = {Text("Codigo Requisitante")},
                        modifier= Modifier.width(400.dp) .focusable(interactionSource = focuSetor))


            }



            Button(acaoAvancarEtapa,modifier= Modifier.align(Alignment.BottomEnd)){
                Text("Proximo")
            }
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuDeSetore(opcoesDeSetores: MutableState<Boolean>,setor: MutableState<String>){
    ExposedDropdownMenuBox(expanded = opcoesDeSetores.value,
                           onExpandedChange = {opcoesDeSetores.value=it},
                           modifier = Modifier.padding(end = 10.dp)){
        OutlinedTextField(value = setor.value,
            onValueChange = {setor.value=it},
            label = {Text("Setor Requisitante")},
            modifier= Modifier.width(400.dp) )
        DropdownMenu(expanded = opcoesDeSetores.value,{opcoesDeSetores.value=!opcoesDeSetores.value},modifier = Modifier.width(400.dp)){
            Row(modifier = Modifier.fillMaxWidth().clickable(onClick = {})) {
                Text("Lavanderia")

            }


        }





    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuLocalidade(opcoesDeLocalidade: MutableState<Boolean>, localidade: MutableState<String>){
    ExposedDropdownMenuBox(expanded = opcoesDeLocalidade.value,
        onExpandedChange = {opcoesDeLocalidade.value=it},
        modifier = Modifier.padding(end = 10.dp)){
        OutlinedTextField(value = localidade.value,
            onValueChange = {localidade.value=it},
            label = {Text("Localidade")},
            modifier= Modifier.width(400.dp) )
        DropdownMenu(expanded = opcoesDeLocalidade.value,{opcoesDeLocalidade.value=!opcoesDeLocalidade.value},modifier = Modifier.width(400.dp)){
            Row(modifier = Modifier.fillMaxWidth().clickable(onClick = {})) {
                Text("Area Limpa")

            }
            Row(modifier = Modifier.fillMaxWidth().clickable(onClick = {})) {
                Text("Area Suja")

            }
            Row(modifier = Modifier.fillMaxWidth().clickable(onClick = {})) {
                Text("Dobra Cme")

            }
            Row(modifier = Modifier.fillMaxWidth().clickable(onClick = {})) {
                Text("Rouparia")

            }
            Row(modifier = Modifier.fillMaxWidth().clickable(onClick = {})) {
                Text("Sala Gerencia")

            }

        }





    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuTipoManutencao(opcoesDeManutencao: MutableState<Boolean>,tipoManutencao: MutableState<String>){
    ExposedDropdownMenuBox(expanded = opcoesDeManutencao.value,
                           onExpandedChange = {opcoesDeManutencao.value=!opcoesDeManutencao.value},
                           modifier = Modifier.padding(end = 10.dp)
    ){
        androidx.compose.material3.OutlinedTextField(value =tipoManutencao.value,
                                                     onValueChange = {tipoManutencao.value=it},
                                                     modifier= Modifier.width(400.dp),
                                                     label = {Text("Tipo de manutencao")} )
        DropdownMenu(expanded = opcoesDeManutencao.value,
            onDismissRequest = {opcoesDeManutencao.value=!opcoesDeManutencao.value},
            modifier = Modifier.width(400.dp)){
            Row(modifier = Modifier.fillMaxWidth().clickable(onClick = {})) {
                Text("Predial ")

            }
            Row(modifier = Modifier.fillMaxWidth().clickable(onClick = {})) {
                Text("Manutencao maquinario")

            }
            Row(modifier = Modifier.fillMaxWidth().clickable(onClick = {})) {
                Text("Manutencao eletrica")

            }


        }
    }

}
@Composable
fun CardDadosDoPedido(modifier: Modifier= Modifier,acaoRetornarEtapa:()-> Unit={},acaoAvancarEtapa: () -> Unit={}){
    val estadoResumo =remember { mutableStateOf("") }
    val estadoDiscricaoDoProblema=remember { mutableStateOf("") }
    OutlinedCard (modifier=modifier.fillMaxWidth(0.7f)) {



        Box(modifier = Modifier.fillMaxSize().padding(10.dp)){

            Column(modifier= Modifier.align(Alignment.TopStart).padding(start = 10.dp).fillMaxWidth()){
                OutlinedTextField(value = estadoResumo.value,
                                  onValueChange = {estadoResumo.value=it},
                                  label = {Text("Resumo do problema")},
                                  maxLines = 2,
                                  modifier = Modifier.fillMaxWidth())
                Spacer(modifier= Modifier.padding(5.dp))
                OutlinedTextField(value = estadoDiscricaoDoProblema.value,
                                  onValueChange = {estadoDiscricaoDoProblema.value=it},
                                  label = {Text("Descricao do problema")},
                                  modifier= Modifier.fillMaxWidth().height(400.dp).padding(bottom = 50.dp))
            }

            Button(onClick = acaoRetornarEtapa,modifier= Modifier.align(Alignment.BottomStart)){
                Text("Voltar")

            }
            Button(acaoAvancarEtapa,modifier= Modifier.align(Alignment.BottomEnd)){
                Text("Proximo")
            }
        }

    }
}
@Composable
fun CardVerificarPedido(modifier: Modifier= Modifier,acaoRetornarEtapa: () -> Unit={}){
    OutlinedCard (modifier=modifier.fillMaxWidth(0.7f).background(Color.White)) {



        Box(modifier = Modifier.fillMaxSize().padding(10.dp)){
            androidx.compose.material3.Text("Verificar")

            Button(onClick = acaoRetornarEtapa,modifier= Modifier.align(Alignment.BottomStart)){
                Text("Voltar")

            }

        }
        }

}

@Composable
fun PainelSoliciTarManutencao(modifier: Modifier= Modifier){
    val controtroleEtapas=rememberEtapasManutencao()
    val  corotineScop=rememberCoroutineScope()
    Column(modifier = modifier.fillMaxWidth()) {
     //  Row(Modifier.fillMaxWidth().align(Alignment.CenterHorizontally), horizontalArrangement = Arrangement.Center) { BarraDeProgressoManutencao(Modifier,controtroleEtapas) }
        Spacer(Modifier.align(Alignment.CenterHorizontally))
        ColetaDeDados(Modifier.align(Alignment.CenterHorizontally).padding(top = 30.dp, bottom = 10.dp),controtroleEtapas.etapa, acaoRetornarEtapa = {
            corotineScop.launch {
                controtroleEtapas.anterior()
            }
        }, acaoAvancarEtapa = {
            corotineScop.launch {
                controtroleEtapas.proximo()
            }
        }
            )
    }
}



@Composable
fun rememberEtapasManutencao(): ControtroleEtapasPedidoManutencao{
    return rememberSaveable(saver = ControtroleEtapasPedidoManutencao.saver() ){
        ControtroleEtapasPedidoManutencao(EtapasPedidoManutencao.EtapaColetarDadosSobreFuncionario)
    }
}

@Composable
fun ItemDeProgressoDeEtapasPedidosDeManutencao(titulo: String,inicio: Boolean,fim: Boolean){
    val scope=rememberCoroutineScope()
    val floatAnimacao= remember { Animatable(0f) }
    val linhaanimada= remember { Animatable(0f) }
    val color= Color.Magenta
    val linhaVisibilidade=remember { mutableStateOf(false) }
    val arcoVisibilidade=remember { mutableStateOf(false) }
    LaunchedEffect(Unit){
        scope.launch {

            if (!inicio){
                delay(200)
                linhaanimada.animateTo(60f)
            }
            delay(500)
            arcoVisibilidade.value=true
            delay(200)
            floatAnimacao.animateTo(360f)
        }
    }
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(40.dp)) {

          Box(Modifier.width(linhaanimada.value.dp).height(5.dp).drawBehind({
              drawRect(brush = SolidColor(color), size = Size(width = size.width, height = 3f))
          }))



   AnimatedVisibility(visible = arcoVisibilidade.value){
       Box(modifier = Modifier.size(40.dp).drawBehind({
           drawArc(brush = SolidColor(color), startAngle = 180f, useCenter = false, size = size, sweepAngle = floatAnimacao.value, style = Stroke(width = 2f, cap = StrokeCap.Round))

       })){
           Text("$titulo", modifier = Modifier.align(Alignment.Center))
       }
   }
   }

}

@Composable
fun ItemDeProgressoDeEtapasPedidosDeManutencao(titulo: String,inicio: Boolean,fim: Boolean,label:@Composable (titulo: String)-> Unit){
    val scope=rememberCoroutineScope()
    val floatAnimacao= remember { Animatable(0f) }
    val linhaanimada= remember { Animatable(0f) }
    val color= Color.Magenta
    val linhaVisibilidade=remember { mutableStateOf(false) }
    val arcoVisibilidade=remember { mutableStateOf(false) }
    LaunchedEffect(Unit){
        scope.launch {

            if (!inicio){
                delay(200)
                linhaanimada.animateTo(60f)
            }
            delay(200)
            arcoVisibilidade.value=true
            delay(200)
            floatAnimacao.animateTo(360f)
        }
    }
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(40.dp)) {

       if(!inicio) {

         Box(Modifier.width(50.dp).height(5.dp).drawBehind({
            drawRect(brush = SolidColor(color), size = Size(width = size.width, height = 3f))
        }))}
         label(titulo)

    }

}
class ControtroleEtapasPedidoManutencao(val inical : EtapasPedidoManutencao){
    val etapa = mutableStateOf<EtapasPedidoManutencao>(inical)
    val fluxoListaDeEtapas= MutableStateFlow< List<EtapasPedidoManutencao>>(listOf(inical))
    fun proximo(){
        val proximaEtapa=etapa.value.proximo()
        if(proximaEtapa!=null){
            adicionarNovaEtapaAlista(proximaEtapa)
            etapa.value=proximaEtapa as EtapasPedidoManutencao

        }
    }
    fun anterior(){
        System.out.println("remover anterior fois chamado")
        val anterior =etapa.value.anterior()
        if(anterior!=null){
            System.out.println("remover anterior fois chamado anterior nao e null")
            removerEtapa()
            etapa.value= anterior as EtapasPedidoManutencao
        }
    }

    private fun adicionarNovaEtapaAlista(proxima: Etapa){

       val mutableList = mutableListOf<EtapasPedidoManutencao>()
        mutableList.addAll(fluxoListaDeEtapas.value)
        mutableList.add(proxima as EtapasPedidoManutencao)
        fluxoListaDeEtapas.value = mutableList.toList()
    }
    private fun removerEtapa(){
        System.out.println("remover etapa fois chamado")
        val mutableList = mutableListOf<EtapasPedidoManutencao>()
        fluxoListaDeEtapas.value.forEachIndexed { id,it->
            if(id!=fluxoListaDeEtapas.value.size-1)
                mutableList.add(it)
        }

        fluxoListaDeEtapas.value = mutableList.toList()
    }

    companion object{
        fun saver (): Saver<ControtroleEtapasPedidoManutencao,*> = Saver({(it.etapa.value as EtapasPedidoManutencao).titulo}, restore = {
            return@Saver when(it){
                EtapasPedidoManutencao.EtapaColetarDadosSobreFuncionario.titulo->{
                    val controle=ControtroleEtapasPedidoManutencao(EtapasPedidoManutencao.EtapaColetarDadosSobreFuncionario )

                    return@Saver controle
                }
                EtapasPedidoManutencao.EtapaVerificarPedido.titulo->{
                    val comtrole= ControtroleEtapasPedidoManutencao(EtapasPedidoManutencao.EtapaVerificarPedido  )

                    return@Saver comtrole
                }
                EtapasPedidoManutencao.EtapaObterDadosDoPedido.titulo->{
                    val comtrole= ControtroleEtapasPedidoManutencao(EtapasPedidoManutencao.EtapaObterDadosDoPedido )

                    return@Saver comtrole
                }

                else -> {
                    val  controle = ControtroleEtapasPedidoManutencao(EtapasPedidoManutencao.EtapaColetarDadosSobreFuncionario )
                    return@Saver controle
                }

            }
        })
    }
}


sealed class EtapasPedidoManutencao(val titulo: String,val inicio: Boolean,val fim: Boolean): Etapa {
    object EtapaColetarDadosSobreFuncionario: EtapasPedidoManutencao("Dados do funcionario",true,false){
        override fun anterior(): Etapa?=null
        override fun proximo(): Etapa? = EtapaObterDadosDoPedido
    }
    object EtapaObterDadosDoPedido: EtapasPedidoManutencao("Pedido",false,false){
        override fun anterior(): Etapa? = EtapaColetarDadosSobreFuncionario
        override fun proximo(): Etapa? = EtapaVerificarPedido
    }
    object EtapaVerificarPedido: EtapasPedidoManutencao("Finalizar",false,true){
        override fun anterior(): Etapa? = EtapaObterDadosDoPedido
        override fun proximo(): Etapa? =null
    }

}