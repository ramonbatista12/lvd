package org.example.project.componentesComponiveis.Paineis.componentesSolicitacoesEpis

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import org.example.project.componentesComponiveis.Paineis.componentesDemanutencao.Etapa
import org.jetbrains.compose.resources.painterResource
import lanvanderia.composeapp.generated.resources.Res
import lanvanderia.composeapp.generated.resources.delete_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import lanvanderia.composeapp.generated.resources.edit_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24

@Composable
fun remenberEstadosSolicitacaoDeIpis() = rememberSaveable(saver = EstadosSolicitacaoDeIpis.saver()){
    EstadosSolicitacaoDeIpis(EtapasSolicitacoesEpis.ObterDados)

}
@Composable
fun remenberEstadosSolicitacaoDeIpis(inicial: EtapasSolicitacoesEpis) = rememberSaveable(saver = EstadosSolicitacaoDeIpis.saver()){

    EstadosSolicitacaoDeIpis(inicial)
}

class EstadosSolicitacaoDeIpis(val inicial : EtapasSolicitacoesEpis = EtapasSolicitacoesEpis.ObterDados){
    val estado = MutableStateFlow<EtapasSolicitacoesEpis>(inicial)
    suspend fun anterior(){
        val anterior = estado.value.anterior()
        if(anterior!=null){
            estado.value= anterior as EtapasSolicitacoesEpis
            return
        }
    }
    suspend fun proximo(){
        System.out.println("clicando em prosimo procesando a prosima etapa")
        val proximo =estado.value.proximo()
        System.out.println("proxima etapa ${proximo}")
        if (proximo!=null){
            estado.value = (proximo as EtapasSolicitacoesEpis)
            return
        }


    }
    fun indiceASerSalvo(): String =estado.value.indice
   companion object{
    fun saver() = object: Saver<EstadosSolicitacaoDeIpis, String>{
            override fun SaverScope.save(value: EstadosSolicitacaoDeIpis): String? {
                return  value.indiceASerSalvo()
            }

        override fun restore(value: String): EstadosSolicitacaoDeIpis? {
           when(value){
               "1" -> return EstadosSolicitacaoDeIpis(EtapasSolicitacoesEpis.ListarPedidos)
               "0" -> return EstadosSolicitacaoDeIpis(EtapasSolicitacoesEpis.ObterDados)
               else -> throw RuntimeException("valor salva nao representa um estado que pode ser salva para representar um estado de Solicitacao de epis ")
           }
        }


    }}

}

sealed class EtapasSolicitacoesEpis(val indice:String): Etapa{
    object ObterDados: EtapasSolicitacoesEpis("0"){
        override fun anterior(): Etapa? = null
        override fun proximo(): Etapa? = ListarPedidos}
    object ListarPedidos: EtapasSolicitacoesEpis("1"){
        override fun anterior(): Etapa? = ObterDados
        override fun proximo(): Etapa? =null}


}

@Composable
fun ColetaDeDadosDoUsuarioParaSolicitarEpi( acacoProximo:()-> Unit){
    OutlinedCard(modifier= Modifier.height(800.dp).width(900.dp).padding(bottom = 20.dp)) {
        val estadoCodigoRequisitante= remember { mutableStateOf("") }
        val estadoMenuSetorDestino =remember { mutableStateOf(false) }
        val setorDestino=remember { mutableStateOf("") }
        val stirngCodigoRequisitante=remember { mutableStateOf("") }

        Box(modifier = Modifier.fillMaxSize().padding(10.dp)){

           Column(modifier=Modifier.align(Alignment.TopStart).padding(bottom = 50.dp, top = 20.dp)) {


            FlowRow {
                MenuSetorDestinoEpi(
                    modifier = Modifier.width(400.dp).padding(end =5.dp),
                    estadoDoMenuDestino = estadoMenuSetorDestino,
                    stringMenuDestino = setorDestino
                )
                OutlinedTextField(
                    value = stirngCodigoRequisitante.value,
                    onValueChange = { stirngCodigoRequisitante.value = it },
                    modifier = Modifier.width(400.dp),
                    maxLines = 1, label = { Text(text = "Codigo do Requisitante") })
            }
        }
            Button(onClick = {
                System.out.println("click prosimo em coletar dados para usuario epi foi acionado")
                acacoProximo()}, modifier = Modifier.align(alignment = Alignment.BottomEnd)){
                Text(text = "Proximo")
            }
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuSetorDestinoEpi(modifier: Modifier= Modifier,
                     estadoDoMenuDestino: MutableState<Boolean>,
                     stringMenuDestino: MutableState<String>){
    ExposedDropdownMenuBox(expanded = estadoDoMenuDestino.value,
                           onExpandedChange = {estadoDoMenuDestino.value=!estadoDoMenuDestino.value},
                           modifier=modifier){
        OutlinedTextField(value = stringMenuDestino.value,
                          onValueChange = {stringMenuDestino.value=it},
                          maxLines = 1,
                          label = {Text("Setor destino")},
                          modifier= Modifier.fillMaxWidth())
        DropdownMenu(expanded = estadoDoMenuDestino.value, onDismissRequest = {estadoDoMenuDestino.value=!estadoDoMenuDestino.value}){
          repeat(5) {
              Row(modifier = Modifier.fillMaxWidth().clickable(onClick = {})) {
              Text(text = "${it}")
            }
          }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuOpcoesDeUnidade(modifier: Modifier= Modifier,
                        estadoDoMenuDestino: MutableState<Boolean>,
                        stringMenuDestino: MutableState<String>){
    ExposedDropdownMenuBox(expanded = estadoDoMenuDestino.value,
        onExpandedChange = {estadoDoMenuDestino.value=!estadoDoMenuDestino.value},
        modifier=modifier){
        OutlinedTextField(value = stringMenuDestino.value,
            onValueChange = {stringMenuDestino.value=it},
            maxLines = 1,
            label = {Text("Unidade")},
            modifier= Modifier.fillMaxWidth())
        DropdownMenu(expanded = estadoDoMenuDestino.value, onDismissRequest = {estadoDoMenuDestino.value=!estadoDoMenuDestino.value}){
            repeat(5) {
                Row(modifier = Modifier.fillMaxWidth().clickable(onClick = {})) {
                    Text(text = "${it}")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuOpcoesDeEpi(modifier: Modifier= Modifier,
                        estadoDoMenuDestino: MutableState<Boolean>,
                        stringMenuDestino: MutableState<String>){
    ExposedDropdownMenuBox(expanded = estadoDoMenuDestino.value,
        onExpandedChange = {estadoDoMenuDestino.value=!estadoDoMenuDestino.value},
        modifier=modifier){
        OutlinedTextField(value = stringMenuDestino.value,
            onValueChange = {stringMenuDestino.value=it},
            maxLines = 1,
            label = {Text("Epi")},
            modifier= Modifier.fillMaxWidth())
        DropdownMenu(expanded = estadoDoMenuDestino.value, onDismissRequest = {estadoDoMenuDestino.value=!estadoDoMenuDestino.value}){
            repeat(5) {
                Row(modifier = Modifier.fillMaxWidth().clickable(onClick = {})) {
                    Text(text = "${it}")
                }
            }
        }
    }
}

@Composable
fun ListarPedidoIpi( acaoAnterior:()-> Unit){
    OutlinedCard(modifier= Modifier.height(800.dp).width(900.dp).padding(bottom = 20.dp)) {
        val stringUnidade=remember { mutableStateOf("") }
        val estadoDoMenuUnidade=remember { mutableStateOf(false) }
        val stringQuantidade =remember { mutableStateOf("") }
        val estadoMenuEpi =remember { mutableStateOf(false) }
        val stringMenuIpi=remember { mutableStateOf("") }
        Box(modifier = Modifier.fillMaxSize().padding(10.dp)){
          Column(modifier=Modifier.align(Alignment.TopStart).padding(bottom = 50.dp, top = 20.dp)) {
              FlowRow() {
                  MenuOpcoesDeEpi(modifier = Modifier.width(400.dp).padding(end = 5.dp),estadoMenuEpi,stringMenuIpi)
                  MenuOpcoesDeUnidade(
                      modifier = Modifier.width(100.dp).padding(end = 5.dp),
                      stringMenuDestino = stringUnidade,
                      estadoDoMenuDestino = estadoDoMenuUnidade
                  )
                  OutlinedTextField(
                      value = stringQuantidade.value,
                      onValueChange = { stringQuantidade.value = it },
                      modifier = Modifier.width(100.dp),
                      maxLines = 1, label = { Text(text = "Qtd") })

              }
              androidx.compose.material.Button(onClick = {}) {
                  Text(text = "adicionar a lista", color = Color.White)


              }
              ListaDeEpisRequisitados(Modifier.padding(top =10.dp).border(width = 1.dp, color = Color.LightGray).fillMaxWidth())


          }



           Button(onClick = {
                System.out.println("click prosimo em coletar dados para usuario epi foi acionado")
                acaoAnterior()}, modifier = Modifier.align(alignment = Alignment.BottomStart)){
                Text(text = "Anterior")

        }
        }
    }
    }


@Composable
fun ListaDeEpisRequisitados(modifier: Modifier= Modifier){
    LazyColumn(modifier = modifier) {
        stickyHeader {
             cabesalhoProdutoEpi(modifier=Modifier.padding(bottom = 20.dp))
        }

       items(20){

            ItemProdutoEpi()
        }


    }


}

@Composable
fun cabesalhoProdutoEpi(modifier: Modifier= Modifier){
    OutlinedCard (modifier = modifier.fillMaxWidth(), shape=RoundedCornerShape(0.dp) ){
        Row(modifier= Modifier.fillMaxSize(),verticalAlignment = Alignment.CenterVertically) {
            Text("Epi Requisitado", Modifier.width(300.dp).padding(end = 5.dp, start = 5.dp))
            Text("Unidade", Modifier.width(100.dp).padding(end = 5.dp))
            Text("quantidade", Modifier.width(100.dp).padding(end = 5.dp))

        }
    }

}

@Composable
fun ItemProdutoEpi(){
    Column(){
        Spacer(modifier = Modifier.padding(top=5.dp))
        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.LightGray)
        Spacer(modifier = Modifier.padding(3.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Epi Requisitado", Modifier.width(300.dp).padding(end = 5.dp, start = 5.dp))
            Text("Unidade", Modifier.width(100.dp).padding(end = 5.dp))
            Text("quantidade", Modifier.width(100.dp).padding(end = 5.dp))
            BotoesDeAcaoParaItemProduto({},{})
        }
    }
}


@Composable
fun BotoesDeAcaoParaItemProduto(acaoEditar:()-> Unit,acaoExcluir:()->Unit){
    FlowRow(){
        OutlinedButton(onClick = acaoExcluir){
            Icon(painter = painterResource(Res.drawable.delete_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                 contentDescription = "")
        }
        OutlinedButton(onClick = acaoEditar){
            Icon(painter = painterResource(Res.drawable.edit_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                contentDescription = "")
        }
    }
}