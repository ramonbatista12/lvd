package org.example.project.componentesComponiveis

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lanvanderia.composeapp.generated.resources.IconeMAquinaDeLavar
import lanvanderia.composeapp.generated.resources.RequesicaoBook
import lanvanderia.composeapp.generated.resources.RequesicaoBook2
import lanvanderia.composeapp.generated.resources.Requesicoes2
import lanvanderia.composeapp.generated.resources.Requisicoesicones
import org.jetbrains.compose.ui.tooling.preview.Preview
import lanvanderia.composeapp.generated.resources.Res
import lanvanderia.composeapp.generated.resources.compose_multiplatform
import lanvanderia.composeapp.generated.resources.Res.drawable
import lanvanderia.composeapp.generated.resources.Solicitacoes
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.skia.Drawable
import lanvanderia.composeapp.generated.resources.allPluralStringResources
import lanvanderia.composeapp.generated.resources.iconeHome
import lanvanderia.composeapp.generated.resources.iconeLogin
import lanvanderia.composeapp.generated.resources.iconeSair
import org.example.project.DestinosDeNavegacao
import org.example.project.repositorio.Login

import org.example.project.viewModel.ViewModelPrincipal
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun BarraLateralPermanente(acaoDeNavegacao:(DestinosDeNavegacao)->Unit,
                           acaoDeLogin: () -> Unit,
                           acaoLogOf: () -> Unit,
                           estadoDoLogin: State<Login>){
    val  clicado = remember { mutableStateOf<DestinosDeNavegacao>(DestinosDeNavegacao.Home) }
    Box(modifier = Modifier.fillMaxHeight()
                           .width(80.dp)
                           .background(color = Color(0xFF00A8FF))
                           ){
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){

            IconesDestinoDeNavegacao.lista.forEach{
                IconeBara(objeto = it, color =Color.White,acao =acaoDeNavegacao, clicado = clicado )

            }

    }
        IconeLogin(stadoDoLogin = estadoDoLogin, acaoLogOf = acaoLogOf,
            acaoDeogin=acaoDeLogin, cor = Color.White, modifier = Modifier.align(Alignment.BottomCenter))


    }
}


sealed class IconesDestinoDeNavegacao(val drawable: DrawableResource,val destino: DestinosDeNavegacao){
       object IconeHome:IconesDestinoDeNavegacao(drawable = Res.drawable.iconeHome,destino = DestinosDeNavegacao.Home)
       object IconeRequesicoes:IconesDestinoDeNavegacao(drawable = Res.drawable.Requesicoes2,destino = DestinosDeNavegacao.Requisicoes)
       object IconeMaquinaDeLavar:IconesDestinoDeNavegacao(drawable = Res.drawable.IconeMAquinaDeLavar,destino = DestinosDeNavegacao.RegistroDeMaquinas)
       object IconeGerenciamento:IconesDestinoDeNavegacao(drawable = Res.drawable.RequesicaoBook2,destino = DestinosDeNavegacao.Gerenciamento)
       object  Solicitacoes:IconesDestinoDeNavegacao(drawable = Res.drawable.Solicitacoes,destino = DestinosDeNavegacao.Solicitacoes)

    companion object{
        val  lista= listOf(IconeHome,IconeRequesicoes,IconeMaquinaDeLavar,IconeGerenciamento,Solicitacoes)
    }


}


@Composable
fun IconeBara(modifier: Modifier=Modifier,color: Color=Color.Black,
              objeto:IconesDestinoDeNavegacao,
              acao:(DestinosDeNavegacao)->Unit={},
              clicado: MutableState<DestinosDeNavegacao>){
  Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
      IconButton(onClick = {
          clicado.value=objeto.destino
          System.out.println("o botaoo do destino ${objeto.destino.toString()} foi clicado")
          acao(objeto.destino)
      },
          modifier=Modifier.size(25.dp)){
       Icon(painter = painterResource(objeto.drawable),
           contentDescription = "",
           modifier =modifier, tint = if(objeto.destino==clicado.value)Color.Black  else color )
    }
      Spacer(Modifier.padding(2.dp))
      AnimatedVisibility(visible = (clicado.value!=objeto.destino)){
          Text(text = "${objeto.destino.toString()}", color = color, fontSize = 12.sp)
      }

   }

}

@Composable
fun IconeLogin(stadoDoLogin: State<Login>,
               acaoLogOf:()-> Unit,
               acaoDeogin:()-> Unit,
               cor: Color,
               modifier: Modifier= Modifier
){
     when(stadoDoLogin.value){
         is Login.Desconectado ->{
             Box(modifier = modifier.fillMaxWidth()
                                    .padding(top = 10.dp).clickable(onClick = {acaoDeogin()})){

                 Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                     Spacer(Modifier.padding(3.dp))
                     Divider(Modifier.fillMaxWidth(), color = cor)
                     Spacer(Modifier.padding(3.dp))
                   Icon(painter = painterResource(Res.drawable.iconeLogin),
                       contentDescription = "",
                       tint = cor,
                       modifier = Modifier.size(25.dp))
                     Spacer(Modifier.padding(3.dp))
                     Text("Login", color = cor)
                     Spacer(Modifier.padding(3.dp))


                 }
             }

         }
         else ->{
             Box(modifier = modifier.fillMaxWidth()
                                    .padding(top = 10.dp).clickable(onClick = {
                                        acaoLogOf()
                 })){
                 Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                     Spacer(Modifier.padding(3.dp))
                     Divider(Modifier.fillMaxWidth(), color = cor)
                     Spacer(Modifier.padding(3.dp))
                     Icon(painter = painterResource(Res.drawable.iconeSair),
                         contentDescription = "",
                         tint = cor,
                         modifier = Modifier.size(25.dp))
                     Spacer(Modifier.padding(3.dp))
                     Text("Sair", color = cor)

                 }
             }
         }
     }
}
@Preview
@Composable
fun previaBarraLateral(){}