package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.ContainerManager
import taboolib.common.io.newFile
import taboolib.common.platform.function.getDataFolder
import taboolib.expansion.*

object ContainerManagerImpl : ContainerManager(), IPersistentContainer {
    override val key = "ContainerManager"
    override val priority = 10
    override val subPouvoir = Pouvoir

    private lateinit var persistentContainer: PersistentContainer
    private var table: String? = null

    private val pouOperator: IContainerOperator<String>
        get() = persistentContainer[table ?: "pouvoir_data"]

    override val container: Container
        get() = persistentContainer.container

    override fun onEnable() {
        persistentContainer = persistentContainer(
            if (Pouvoir.configManager["config"].getBoolean("database.enable")) {
                Pouvoir.config.getConfigurationSection("database")!!.also {
                    table = it.getString("table")
                }
            } else {
                newFile(getDataFolder(), "data.db")
            }
        ) {
            strContainer(table ?: "pouvoir_data")
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
        return pouOperator[user][key]?.toString()
    }

    override fun delete(user: String, key: String) {
        set(user, key, null)
    }

    override operator fun set(user: String, key: String, value: Any?) {
        pouOperator[user] = mapOf("key" to key, "value" to value)
    }


}