package com.skillw.pouvoir.api.container

import com.skillw.pouvoir.internal.feature.container.PersistentContainer

interface IPersistentContainer {
    val container: Container

    /** 注册标准容器 */
    fun container(table: String, server: Boolean = false, builder: ContainerBuilder.() -> Unit)

    /** 注册标准容器 */
    fun strContainer(table: String)

    /** 注册扁平容器 */
    fun flatContainer(table: String, builder: ContainerBuilder.Flatten.() -> Unit = {})

    companion object {

        /** 创建持久化储存容器 */
        @JvmStatic
        fun persistentContainer(type: Any, builder: PersistentContainer.() -> Unit): PersistentContainer {
            return PersistentContainer(type, builder)
        }

        /** 创建持久化储存容器 */
        @JvmStatic
        fun persistentContainer(
            host: String,
            port: Int,
            user: String,
            password: String,
            database: String,
            builder: PersistentContainer.() -> Unit,
        ): PersistentContainer {
            return PersistentContainer(host, port, user, password, database, builder)
        }
    }

}