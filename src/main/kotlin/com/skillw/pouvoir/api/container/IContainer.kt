package com.skillw.pouvoir.api.container

import com.skillw.pouvoir.api.able.Registrable
import com.skillw.pouvoir.api.plugin.SubPouvoir

interface IContainer : Registrable<String>, IPersistentContainer {
    val subPouvoir: SubPouvoir
    var table: String?
    val defaultOperator: IContainerOperator<String>
    override val container: Container

    override fun container(table: String, server: Boolean, builder: ContainerBuilder.() -> Unit)

    override fun strContainer(table: String)

    override fun flatContainer(table: String, builder: ContainerBuilder.Flatten.() -> Unit)

    operator fun get(user: String, key: String): String?
    fun delete(user: String, key: String)

    operator fun set(user: String, key: String, value: Any?)
}