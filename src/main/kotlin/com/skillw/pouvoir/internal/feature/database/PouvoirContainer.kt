package com.skillw.pouvoir.internal.feature.database

import com.skillw.pouvoir.Pouvoir.databaseManager
import com.skillw.pouvoir.api.feature.database.UserBased
import com.skillw.pouvoir.internal.manager.PouConfig
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.warning

object PouvoirContainer : UserBased {
    @JvmStatic
    val holder by lazy {
        databaseManager.containerHolder(PouConfig.databaseConfig)
    }

    @JvmStatic
    lateinit var container: UserBased

    @Awake(LifeCycle.ENABLE)
    fun loadContainer() {
        kotlin.runCatching {
            if (holder == null) warning("Pouvoir User Container holder is null!")
            val container = holder?.container("pouvoir_data", true)
            if (container == null) warning("Pouvoir User Container container is null!")
            this.container = (container as? UserBased?)!!
        }.let {
            if (it.isFailure){
                warning("Pouvoir User Container Initialization Failed!")
                it.exceptionOrNull()?.printStackTrace()
            }

        }
    }

    override fun get(user: String, key: String): String? {
        return container[user, key]
    }

    override fun delete(user: String, key: String) {
        return container.delete(user, key)
    }

    override fun set(user: String, key: String, value: String?) {
        container[user, key] = value
    }

    override fun contains(user: String, key: String): Boolean {
        return container.contains(user, key)
    }
}