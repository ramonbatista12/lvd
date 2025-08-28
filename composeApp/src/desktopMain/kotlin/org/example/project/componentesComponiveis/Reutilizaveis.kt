package org.example.project.componentesComponiveis

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.insert
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.placeCursorAtEnd
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.then
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import lanvanderia.composeapp.generated.resources.IconeDeRetorno
import org.jetbrains.compose.resources.painterResource
import lanvanderia.composeapp.generated.resources.Res
import javax.swing.GroupLayout


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
fun EntradaDeTextoBaraPesquiFormatoDeData(modifier: Modifier= Modifier,
                                          acaoDeExpandir:(Boolean)-> Unit,
                                          expandido: Boolean,
                                          iconePesquisa:@Composable (Modifier)-> Unit,
                                          iconeDeFiltro:@Composable (Modifier)-> Unit,
                                          iconeDeClose:@Composable (Modifier,calback:()-> Unit)-> Unit){
    val recursoDeInteracao = remember{ MutableInteractionSource() }//representa o recurso de intercao
    val emfocu =recursoDeInteracao.collectIsFocusedAsState()//coleta um estado para saber se esta focado
    val focusRequester =remember { FocusRequester() }// requisitante de foco
    val gerenciadorDeFoco = LocalFocusManager.current// gerenciador de foco
    val textFildState = rememberTextFieldState()

        TextField(state = textFildState,
                  modifier= Modifier.padding(start = 20.dp)
                           .focusRequester(focusRequester = focusRequester)
                                        .onFocusChanged({
                                            if(it.isFocused)acaoDeExpandir(true)
                                            else acaoDeExpandir(false)
                                        }),
                                        lineLimits = TextFieldLineLimits.SingleLine,
                                        inputTransformation = InputTransformation.maxLength(FormatadoresDeTexto.tamanhoData)
                                                                                 .then(FormatadoresDeTexto.InputTransformationData()),
                                        outputTransformation = FormatadoresDeTexto.OutputTrasnformatinData(),
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
          val textoLinpo=auxiliar.replace(Regex("^[a-zA-z]"), transform = {""})
          replace(0,length,textoLinpo)
          }}
    class OutputTrasnformatinData: OutputTransformation{
        override fun TextFieldBuffer.transformOutput() {
            System.out.println(" data putputi trasformation")
         if(length>2)insert(2,"/")
         if(length>4) insert(5,"/")
         if(length>0)   this.placeCursorAtEnd()
        }
    }
}
