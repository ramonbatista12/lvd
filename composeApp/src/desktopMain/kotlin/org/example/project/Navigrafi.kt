package org.example.project

import kotlinx.serialization.Serializable


sealed class DestinosDeNavegacao(){
    @Serializable
    object Home: DestinosDeNavegacao()
    @Serializable
    object Solicitacoes: DestinosDeNavegacao()
    @Serializable
    object RegistroDeMaquinas: DestinosDeNavegacao()
    @Serializable
    object Requisicoes: DestinosDeNavegacao()
    @Serializable
    object Gerenciamento: DestinosDeNavegacao()

    override fun toString(): String {
        when(this){
            is Home->return "Home"
            is Solicitacoes->return "Solicitacoes"
            is RegistroDeMaquinas->return "Maquinas"
            is Requisicoes->return "Requisicoes"
            is Gerenciamento->return "Gerencia"
            else -> return "Home"
        }
    }

    sealed class DestinosDeDialogo(): DestinosDeNavegacao(){
        @Serializable
        object DialogoDeLogin: DestinosDeDialogo()
        @Serializable
        object DialogoDeComfirmacao: DestinosDeDialogo()

    }

}