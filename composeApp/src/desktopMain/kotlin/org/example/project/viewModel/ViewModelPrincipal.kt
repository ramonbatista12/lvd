package org.example.project.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.repositorio.Login

class ViewModelPrincipal: ViewModel() {
    private val corotine = viewModelScope
   private val _fluxoDeLogin = MutableStateFlow<Login>(Login.Desconectado)
   val fluxoDeLogin =_fluxoDeLogin.asStateFlow()

    fun logar(login: Login){
        corotine.launch {
            System.out.println("login sendo  executado logando como $login")
            _fluxoDeLogin.emit(login)
        }
    }

    fun  desconectar(){
        corotine.launch {
            _fluxoDeLogin.emit(Login.Desconectado)
        }
    }

}

