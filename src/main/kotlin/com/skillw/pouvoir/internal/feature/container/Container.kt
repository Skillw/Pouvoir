package com.skillw.pouvoir.internal.feature.container

import com.skillw.pouvoir.Pouvoir.containerManager
import com.skillw.pouvoir.api.container.Container
import com.skillw.pouvoir.api.container.ContainerBuilder
import com.skillw.pouvoir.api.container.IContainer
import com.skillw.pouvoir.api.container.IContainerOperator
import com.skillw.pouvoir.api.container.IPersistentContainer.Companion.persistentContainer
import com.skillw.pouvoir.api.manager.ConfigManager
import com.skillw.pouvoir.api.plugin.SubPouvoir
import taboolib.common.io.newFile
import java.io.File

/**
 * @className Container
 *
 * @author Glom
 * @date 2022/8/12 22:05 Copyright 2022 user. All rights reserved.
 */
class Container(override val subPouvoir: SubPouvoir) : IContainer {
    override val key: String = subPouvoir.key
    private lateinit var persistentContainer: PersistentContainer
    override var table: String? = null

    override val defaultOperator: IContainerOperator<String> by lazy {
        persistentContainer[table ?: "${key.lowercase()}_data"]
    }

    override val container: Container
        get() = persistentContainer.container

    fun init() {
        try {
            val configManager = (subPouvoir.managerData["ConfigManager"] as ConfigManager)
            val config = configManager["config"]
            if (!config.isConfigurationSection("database")) {
                config.set(
                    "database", linkedMapOf(
                        "enable" to "false",
                        "host" to "localhost",
                        "port" to "3306",
                        "user" to "root",
                        "password" to "root",
                        "database" to "root",
                        "table" to "my_database"
                    )
                )
                config.save(File(subPouvoir.plugin.dataFolder, "config.yml"))
            }
            persistentContainer = persistentContainer(
                if (config.getBoolean("database.enable")) {
                    config.getConfigurationSection("database")!!.also {
                        table = it.getString("table")
                    }
                } else {
                    newFile(subPouvoir.plugin.dataFolder, "data.db")
                }
            ) {
                strContainer(table ?: "${key.lowercase()}_data")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun container(table: String, server: Boolean, builder: ContainerBuilder.() -> Unit) {
        persistentContainer.container(table, server, builder)
    }

    override fun strContainer(table: String) {
        persistentContainer.strContainer(table)
    }

    override fun flatContainer(table: String, builder: ContainerBuilder.Flatten.() -> Unit) {
        persistentContainer.flatContainer(table, builder)
    }

    inline operator fun <reified T> get(name: String): IContainerOperator<T> {
        return container.operator(name) as? IContainerOperator<T>?
            ?: error("No such a IContainerOperator with ${T::class.java.name}")
    }

    override operator fun get(user: String, key: String): String? {
        return defaultOperator[user][key]?.toString()
    }

    override fun delete(user: String, key: String) {
        set(user, key, null)
    }

    override operator fun set(user: String, key: String, value: Any?) {
        defaultOperator[user] = mapOf("key" to key, "value" to value)
    }

    override fun register() {
        containerManager.register(this)
    }
}