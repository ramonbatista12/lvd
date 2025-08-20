package org.example.project.repositorio

import androidx.compose.ui.geometry.Offset
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PagingState
import app.cash.paging.PagingSource
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.statements.UpsertSqlExpressionBuilder.greater
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

val pagerSize =30

fun List<EntidadeDataRegistro>.prosimaChave(): Int?{
    if(this.isEmpty())return null
    val ultimoid =this[this.lastIndex].id
    return ultimoid.value
}
fun List<EntidadeDataRegistro>.chaveAnterior():Int?{
    if(this.isEmpty()) return null
    return this[0].id.value
}
class RecursoDePaginacaoDeDatasDeRegistroDeMaquinas: PagingSource<Int, EntidadeDataRegistro>() {
    var chaveAnterior =0
    var refresh =0
init {
    System.out.println("recuro de paginas iniciado ")
}
    override fun getRefreshKey(state: PagingState<Int, EntidadeDataRegistro>): Int? {
        val posicao = state.anchorPosition ?: return null
        return  state.closestPageToPosition(posicao)?.nextKey ?: state.closestPageToPosition(posicao)?.prevKey
     }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EntidadeDataRegistro> {
        val chaveAtual = params.key?:0
        when(params){
            is LoadParams.Prepend<*> -> {
            }
            is LoadParams.Refresh<*> -> {}
            is LoadParams.Append<*> -> {}
        }

        try {
         val  lista =  if(chaveAtual==0) primeiraBusca(params.loadSize)
                     else busca(chaveAtual,params.loadSize)
         return LoadResult.Page(data = lista,
                                nextKey =if(lista.isEmpty()) null else chaveAtual+params.loadSize ,
                                prevKey = if(chaveAtual==0)null else chaveAtual-params.loadSize)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }


            private suspend fun primeiraBusca(limite: Int): List<EntidadeDataRegistro> {
                System.out.println("\nessa e a primeira busca ")
                return transaction {
                    EntidadeDataRegistro.all().orderBy(Pair(TabelaDeDatasDeRegistros.id, SortOrder.DESC)).limit(limite)
                        .toList()
                }
            }
    private suspend fun busca(offset: Int,limite: Int): List<EntidadeDataRegistro> {
        System.out.println("\nessa e a primeira busca ")
        return transaction {
            EntidadeDataRegistro.all().orderBy(Pair(TabelaDeDatasDeRegistros.id, SortOrder.DESC)).offset(offset.toLong()).limit(limite)
                .toList()
        }
    }



    }

sealed class EstadoPadinacao(){
    object PrimeiraBusca: EstadoPadinacao()
    object DecendoNaLista: EstadoPadinacao()
    object subindoNaLista: EstadoPadinacao()

}