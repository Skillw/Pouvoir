package com.skillw.pouvoir.api.feature.database

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.plugin.map.DataMap
import com.skillw.pouvoir.api.plugin.map.component.Registrable
import java.util.*

/**
 * @className DatabaseType
 *
 * 数据库类型，主要用来管理ContainerHolder
 *
 * 负责数据库的创建，链接，销毁
 *
 * @author Glom
 * @date 2023/1/12 18:53 Copyright 2024 Glom. 
 */
abstract class DatabaseType<H : ContainerHolder<C>, C : BaseContainer>(override val key: String) : Registrable<String> {
    /**
     * Connect
     *
     * @param params
     * @return
     */
    protected abstract fun connect(params: DataMap): H

    protected val holders = HashSet<H>()

    fun connectWith(params: DataMap): H {
        return connect(params)
    }

    fun containers(): List<BaseContainer> {
        val containers = LinkedList<BaseContainer>()
        holders.forEach { containers += it.values }
        return containers
    }

    fun disconnectAll() {
        holders.forEach(ContainerHolder<C>::disconnectAll)
    }

    override fun register() {
        Pouvoir.databaseManager.register(this)
    }
}