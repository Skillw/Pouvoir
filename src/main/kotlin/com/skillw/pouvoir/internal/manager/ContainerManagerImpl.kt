package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.manager.sub.ContainerManager
import com.skillw.pouvoir.internal.feature.container.Container

object ContainerManagerImpl : ContainerManager() {
    override val key = "ContainerManager"
    override val priority = 10
    override val subPouvoir = Pouvoir

    @AutoRegister
    val container = Container(Pouvoir)

    override fun get(user: String, key: String): String? {
        return container[user, key]
    }

    override fun delete(user: String, key: String) {
        container.delete(user, key)
    }

    override fun set(user: String, key: String, value: Any?) {

        container[user, key] = value
    }

    override fun onEnable() {
        values.forEach {
            it.init()
        }
    }
}