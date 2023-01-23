package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.feature.database.BaseContainer
import com.skillw.pouvoir.api.feature.database.ContainerHolder
import com.skillw.pouvoir.api.manager.sub.DatabaseManager
import com.skillw.pouvoir.api.plugin.map.DataMap

internal object DatabaseManagerImpl : DatabaseManager() {
    override val key = "DatabaseManager"
    override val priority = 10
    override val subPouvoir = Pouvoir

    override fun containerHolder(data: DataMap): ContainerHolder<*>? {
        val type = data.get("type", "sqlite")
        return get(type)?.connectWith(data)
    }

    override fun onActive() {
        values.forEach {
            it.containers().forEach(BaseContainer::onActive)
        }
    }

    override fun onReload() {
        values.forEach {
            it.containers().forEach(BaseContainer::onReload)
        }
    }

    override fun onDisable() {
        values.forEach {
            it.containers().forEach(BaseContainer::onDisable)
        }
    }
}