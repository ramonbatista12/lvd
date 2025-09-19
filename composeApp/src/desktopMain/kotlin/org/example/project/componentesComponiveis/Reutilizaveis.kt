package org.example.project.componentesComponiveis

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.insert
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.placeCursorAtEnd
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.then
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import lanvanderia.composeapp.generated.resources.IconeDeRetorno
import org.jetbrains.compose.resources.painterResource
import lanvanderia.composeapp.generated.resources.Res
import org.example.project.componentesComponiveis.Paineis.componentesRegistroDeMaquinas.EstadoFiltroPesquisa
import org.example.project.componentesComponiveis.Paineis.componentesRegistroDeMaquinas.FiltroDePesquisaSelelcionado


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


@Composable
fun EntradaDeTextoBaraPesquiPorFiltro(textFildState: TextFieldState=rememberTextFieldState(),
                                      filtroDePesquisaSelelcionado: FiltroDePesquisaSelelcionado,
                                          modifier: Modifier= Modifier,
                                      acaoDeExpandir:(Boolean)-> Unit,
                                      expandido: Boolean,
                                      iconePesquisa:@Composable (Modifier)-> Unit,
                                      iconeDeFiltro:@Composable (Modifier)-> Unit,
                                      iconeDeClose:@Composable (Modifier,calback:()-> Unit)-> Unit,
                                      acaoDePesquisa:(String)-> Unit){
    val recursoDeInteracao = remember{ MutableInteractionSource() }//representa o recurso de intercao
    val emfocu =recursoDeInteracao.collectIsFocusedAsState()//coleta um estado para saber se esta focado
    val focusRequester =remember { FocusRequester() }// requisitante de foco
    val gerenciadorDeFoco = LocalFocusManager.current// gerenciador de foco
    val filtroDePesquisaSelelcionado=filtroDePesquisaSelelcionado.estado.collectAsState()
        LaunchedEffect(textFildState.text){
            acaoDePesquisa(textFildState.text.toString())
        }
        TextField(state = textFildState,
                  modifier= Modifier.padding(start = 20.dp)
                           .focusRequester(focusRequester = focusRequester)
                                        .onFocusChanged({
                                            if(it.isFocused)acaoDeExpandir(true)
                                            else acaoDeExpandir(false)
                                        }),
                                        lineLimits = TextFieldLineLimits.SingleLine,
                                        inputTransformation =
                                            InputTransformation.maxLength(FormatadoresDeTexto.tamanhoData)
                                                                                               .then(FormatadoresDeTexto.InputTransformationData()),
                                        outputTransformation = when(filtroDePesquisaSelelcionado.value){
                                                                           is EstadoFiltroPesquisa.Data ->   FormatadoresDeTexto.OutputTrasnformatinData()
                                                                           else -> null
                                                                                                       },
                  leadingIcon = {iconePesquisa(Modifier)},
                  trailingIcon = {
                              Row(horizontalArrangement = Arrangement.Center ){
                                  if(expandido){
                                  iconeDeFiltro(Modifier)
                                  iconeDeClose(Modifier,{ textFildState.clearText()})}
                                }},
                  colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colorScheme.background )



                       )



    LaunchedEffect(emfocu.value){
        System.out.println("o valor dde em foco mudou ${emfocu}")
    }
     val limparfoco =!expandido && emfocu.value
    LaunchedEffect(expandido){
        if(limparfoco){
            gerenciadorDeFoco.clearFocus()
        }
    }

}



object FormatadoresDeTexto{
    final val tamanhoData =8
    class InputTransformationData: InputTransformation  {
        override fun TextFieldBuffer.transformInput() {
          val auxiliar = asCharSequence()
          val textoLinpo=auxiliar.replace(Regex("\\D"), transform = {""})
          replace(0,length,textoLinpo)
          }}
    class OutputTrasnformatinData: OutputTransformation{
        override fun TextFieldBuffer.transformOutput() {

         if(length>2)insert(2,"/")
         if(length>4) insert(5,"/")
         if(length>0)   this.placeCursorAtEnd()
        }
    }

    class InputTrasformationFiltroNumerico: InputTransformation{
        override fun TextFieldBuffer.transformInput() {
            val textolimpos = asCharSequence().replace(Regex("^[a-zA-Z]"), transform = {""})
            replace(0,length,textolimpos)
        }
    }
    class InputTrasformationFiltroPeso: InputTransformation{
        override fun TextFieldBuffer.transformInput() {
            val textoLinpo = asCharSequence().replace(Regex("\\D"), transform = {""})
            val textoformatado =if(textoLinpo=="")0 else textoLinpo.toInt()
            replace(0,length,(textoformatado/10f).toString())
        }
    }
    class InputTrasFormationDropDawMenuBox: InputTransformation{
        override fun TextFieldBuffer.transformInput() {
            if(this.originalText.contains(Regex("d|w|s|D|W|s"))) this.revertAllChanges()
        }
    }
    class OutputTransformationPeso: OutputTransformation{
        override fun TextFieldBuffer.transformOutput() {
            val peso =if(originalText!="") this.originalText.toString().toInt() else 0
            val pesoFOrmatado= peso/10F


        }
    }



}
