package org.example.project.repositorio.adapitadoreDeTabelas

import kotlinx.coroutines.sync.Mutex
import java.security.MessageDigest
import java.security.MessageDigestSpi

object ObjetoAdaitadorUsuarios{
    val mutex = Mutex()



    internal class ValiDadoRDeSenhas{
        val algoritimo = MessageDigest.getInstance("SHA-256")

        suspend fun comparar(senha:String,sha256: String): Boolean{
            try {
                val criptografado=algoritimo.digest(senha.toByteArray()).toString()
                if(sha256.equals(criptografado)) return true
            }finally {
                return false
            }

        }
        suspend fun sifrarSenha(senha:String):String{
            try {
                val criptografrado =algoritimo.digest(senha.toByteArray()).toString()
                return criptografrado
            }finally {
                return ""
            }
        }
    }
}