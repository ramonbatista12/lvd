package org.example.project.repositorio

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.javatime.date
import org.jetbrains.exposed.v1.javatime.time


object TabelaUsuarios: IdTable<Int>("usuarios"){
    override val id = integer("id_usuario").autoIncrement().entityId()
    val nome = varchar("nome_usuario",200)
    val codigoUsuaria=integer("codigo_usuario").uniqueIndex()
    val senha =varchar("senha",100)
    val funcao =integer("funcao").references(TabelaDeFuncoes.id)
    val ativo =bool("ativo")
    override val primaryKey = PrimaryKey(id)
}//

object TabelaDeFuncoes: IdTable<Int>("funcoes"){
    override val id =integer("id_funcao").autoIncrement().entityId()
    val nomeDaFuncao=varchar("nome_da_funcao",100)
    val descricaoDaFuncao=varchar("descricao_da_funcao",300)
    val ativo =bool("ativa")
    override val primaryKey= PrimaryKey(id)
}


object TabelaDeDatasDeRegistros: IdTable<Int>("data_registro_maquinas"){
    override val id =integer("id_data_registro").autoIncrement().entityId()
    override val primaryKey =PrimaryKey(id)
    val data = date("data_registro")
    val estaDoFinalizacao=bool("finalizada")
}

object TabelaDeRegistroDeMaquinas: IdTable<Int>("registro_de_maquinas"){
    override val id= integer("id_registro").autoIncrement().entityId()
    override val primaryKey=PrimaryKey(id)
    val codigoDoOperar =integer("id_codigo_usuario").references(TabelaUsuarios.codigoUsuaria)
    val peso = float("peso")
    val dataFinalizacao =date("dataFinalizacao").nullable()
    val horaEntrada =time("hora_entrada")
    val horaSaida= time("hora_saida").nullable()
    val finalizada =bool("finalizada").default(false)
    val processo=integer("idProcesso").references(TabelaProcessos.id)
    val maquina =integer("idMaquina").references(TabelaDeMaquinas.id)
    val tipoDeRoupa =integer("idTipo").references(TabelaTipoDeRoupas.id)
    val data =integer("id_data_registro").references(TabelaDeDatasDeRegistros.id)
}

object TabelaProcessos: IdTable<Int>("tabela_processos"){
    override val id=integer("id_processo").autoIncrement().entityId()
    override val primaryKey = PrimaryKey(id)
    val nome = varchar("nome_processo",100)
    val descricao=varchar("descricao_do_processo",200)
    val numero =integer("numero")

}

object TabelaDeMaquinas: IdTable<Int>("tabela_de_maquina"){
    override val id =integer("id_maquina").autoIncrement().entityId()
    override val primaryKey =PrimaryKey(id)
    val numeroDaMaquina=integer("numero_Da_maquina")
    val pesoMasimo=float("peso_masimo")
    val pesoMinimo=float("peso_minimo")

}//



object TabelaTipoDeRoupas: IdTable<Int>("tabela_tipo_de_roupas"){
    override val id =integer("id_tipo").autoIncrement().entityId()
    override val primaryKey =PrimaryKey(id)
    val nomeTipo =varchar("tipo",100)
    val descricao =varchar("descricao_do_tipo",200)

}
object TabelaInvalidacoes: IntIdTable("invalidacao","id_invalidacao"){
    val nomeDaTabela=varchar("nome_da_tabela",100)
    val invalidada =bool("invalidada")
}

class EntidadeRegistroDeMaquinas(id: EntityID<Int>): Entity<Int>(id){
    companion object : EntityClass<Int, EntidadeRegistroDeMaquinas>(TabelaDeRegistroDeMaquinas)
    var idRegistro by TabelaDeRegistroDeMaquinas.id
    var codOperador by TabelaDeRegistroDeMaquinas.codigoDoOperar
    var idProcesso by TabelaDeRegistroDeMaquinas.processo
    var idTipoDeRoupas by TabelaDeRegistroDeMaquinas.tipoDeRoupa
    var idData by TabelaDeRegistroDeMaquinas.data
    var peso by TabelaDeRegistroDeMaquinas.peso
    var dataFinalizacao by TabelaDeRegistroDeMaquinas.dataFinalizacao
    var maquina by TabelaDeRegistroDeMaquinas.maquina
    var entrada by TabelaDeRegistroDeMaquinas.horaEntrada
    var saida by TabelaDeRegistroDeMaquinas.horaSaida
    var finalizada by TabelaDeRegistroDeMaquinas.finalizada
    var tipo by EntidadeTipoDeRoupas referencedOn TabelaDeRegistroDeMaquinas.tipoDeRoupa
    var processo by EntidadeTabelaProcesso referencedOn TabelaDeRegistroDeMaquinas.processo
    var operador by EntidadeUsuarios referencedOn TabelaDeRegistroDeMaquinas.codigoDoOperar
    var maquinaUsada by EntidadeTabelaDeMaquinas referencedOn TabelaDeRegistroDeMaquinas.maquina
}

class EntidadeDataRegistro(id: EntityID<Int>): Entity<Int>(id){
    companion object: EntityClass<Int, EntidadeDataRegistro>(TabelaDeDatasDeRegistros)
    var id_data by TabelaDeDatasDeRegistros.id
    var data by TabelaDeDatasDeRegistros.data
    var esado by TabelaDeDatasDeRegistros.estaDoFinalizacao
}

class EntidadeTabelaProcesso(id: EntityID<Int>): Entity<Int>(id){
    companion object : EntityClass<Int, EntidadeTabelaProcesso>(TabelaProcessos)
    var idProcesso by TabelaProcessos.id
    var nome by TabelaProcessos.nome
    var descricao by TabelaProcessos.descricao
    var numero by TabelaProcessos.numero
}
class EntidadeTabelaDeMaquinas(id: EntityID<Int>): Entity<Int>(id){
    companion object : EntityClass<Int, EntidadeTabelaDeMaquinas>(TabelaDeMaquinas)
    var idMaquna by TabelaDeMaquinas.id
    var numeroDaMaquina by TabelaDeMaquinas.numeroDaMaquina
    var pesoMasimo by TabelaDeMaquinas.pesoMasimo
    var pesoMinimo by TabelaDeMaquinas.pesoMinimo

}
class EntidadeUsuarios(id: EntityID<Int>): Entity<Int>(id){
    companion object : EntityClass<Int, EntidadeUsuarios>(TabelaUsuarios)
    var id_usuario = TabelaUsuarios.id
    var nome by TabelaUsuarios.nome
    var senha by TabelaUsuarios.senha
    var codigo by TabelaUsuarios.codigoUsuaria
    var codigoFuncao by TabelaUsuarios.funcao

    val funcao by EntidadeFuncoes referencedOn TabelaUsuarios.funcao

}

class EntidadeTipoDeRoupas(id: EntityID<Int>): Entity<Int>(id){
    companion object : EntityClass<Int, EntidadeTipoDeRoupas>(TabelaTipoDeRoupas)
    var idTipo by TabelaTipoDeRoupas.id
    var nome by TabelaTipoDeRoupas.nomeTipo
    var descricao by TabelaTipoDeRoupas.descricao
}


class EntidadeFuncoes(id: EntityID<Int>): Entity<Int>(id){
    companion object: EntityClass<Int, EntidadeFuncoes>(TabelaDeFuncoes)
    var idFuncao by TabelaDeFuncoes.id
    var nomeDaFuncao by TabelaDeFuncoes.nomeDaFuncao
    var descricao by TabelaDeFuncoes.descricaoDaFuncao
}

class EntidadeInvalidacao(id: EntityID<Int>): IntEntity(id){
    companion object : IntEntityClass<EntidadeInvalidacao>(TabelaInvalidacoes)
    var idInvalidacao by TabelaInvalidacoes.id
    var nome by TabelaInvalidacoes.nomeDaTabela
    var invalidada by TabelaInvalidacoes.invalidada

}