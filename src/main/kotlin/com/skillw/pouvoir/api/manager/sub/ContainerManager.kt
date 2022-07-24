package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.Manager
import org.bukkit.entity.Entity
import taboolib.expansion.IPersistentContainer

abstract class ContainerManager : Manager, IPersistentContainer {
    abstract operator fun get(user: String, key: String): String?
    abstract fun delete(user: String, key: String)
    abstract operator fun set(user: String, key: String, value: Any?)


    companion object {
        @JvmStatic
        fun Entity.getData(key: String): String? {
            return Pouvoir.containerManager[uniqueId.toString(), key]
        }

        @JvmStatic
        fun Entity.deleteData(key: String) {
            return Pouvoir.containerManager.delete(uniqueId.toString(), key)
        }

        @JvmStatic
        fun Entity.setData(key: String, value: String) {
            Pouvoir.containerManager[uniqueId.toString(), key] = value
        }
    }
}