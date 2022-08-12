package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.Manager
import org.bukkit.entity.Entity
import taboolib.expansion.IPersistentContainer

/**
 * Container manager
 *
 * @constructor Create empty Container manager
 */
abstract class ContainerManager : Manager, IPersistentContainer {
    /**
     * Get
     *
     * @param user 用户名
     * @param key 键
     * @return 数据库中获取到的值
     */
    abstract operator fun get(user: String, key: String): String?

    /**
     * Delete
     *
     * @param user 用户名
     * @param key 键
     */
    abstract fun delete(user: String, key: String)

    /**
     * FunctionSet
     *
     * @param user 用户名
     * @param key 键
     * @param value 值
     */
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