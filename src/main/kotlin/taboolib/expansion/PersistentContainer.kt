package taboolib.expansion

import taboolib.common.io.newFile
import taboolib.common.platform.function.getDataFolder
import taboolib.library.configuration.ConfigurationSection
import java.io.File

/**
 * 创建持久化储存容器
 */
fun persistentContainer(type: Any, builder: PersistentContainer.() -> Unit): PersistentContainer {
    return PersistentContainer(type, builder)
}

/**
 * 创建持久化储存容器
 */
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

interface IPersistentContainer {
    val container: Container

    /**
     * 注册标准容器
     */
    fun container(table: String, server: Boolean = false, builder: ContainerBuilder.() -> Unit)

    /**
     * 注册标准容器
     */
    fun strContainer(table: String)

    /**
     * 注册扁平容器
     */
    fun flatContainer(table: String, builder: ContainerBuilder.Flatten.() -> Unit = {})
}

class PersistentContainer : IPersistentContainer {

    override val container: Container

    /**
     * 设置源
     * - 传入文件类型则为 SQLite 模式
     * - 传入 ConfigurationSection 则读取 SQL 配置
     */
    constructor(type: Any, builder: PersistentContainer.() -> Unit) {
        this.container = when (type) {
            is File -> {
                ContainerSQLite(type)
            }

            is String -> {
                ContainerSQLite(newFile(getDataFolder(), type))
            }

            is ConfigurationSection -> {
                ContainerSQL(
                    type.getString("host", "localhost")!!,
                    type.getInt("port"),
                    type.getString("user", "user")!!,
                    type.getString("password", "user")!!,
                    type.getString("database", "minecraft")!!
                )
            }

            else -> error("Unsupported source type: $type")
        }
        builder(this)
        this.container.init()
    }

    /**
     * 设置 SQL 源
     */
    constructor(
        host: String,
        port: Int,
        user: String,
        password: String,
        database: String,
        builder: PersistentContainer.() -> Unit
    ) {
        this.container = ContainerSQL(host, port, user, password, database)
        builder(this)
        this.container.init()
    }

    /**
     * 注册标准容器
     */
    override fun container(table: String, server: Boolean, builder: ContainerBuilder.() -> Unit) {
        container.addTable(
            table,
            player = !server,
            playerKey = true,
            data = ContainerBuilder(table).also(builder).dataList
        )
    }

    /**
     * 注册标准容器
     */
    override fun strContainer(table: String) {
        container.addTable(
            table,
            player = true,
            playerKey = true,
            data = ContainerBuilder(table).run {
                data("key")
                data("value")
                this
            }.dataList
        )
    }

    /**
     * 注册扁平容器
     */
    override fun flatContainer(table: String, builder: ContainerBuilder.Flatten.() -> Unit) {
        container.addTable(
            table,
            player = true,
            playerKey = false,
            data = ContainerBuilder.Flatten(table).also(builder).fixed().dataList
        )
    }

    /**
     * 获取控制器
     */
    inline operator fun <reified T> get(table: String): IContainerOperator<T> {
        return container.operator(table) as? IContainerOperator<T>?
            ?: error("No such a IContainerOperator with ${T::class.java.name}")
    }

    /**
     * 关闭容器
     */
    fun close() {
        container.close()
    }
}

