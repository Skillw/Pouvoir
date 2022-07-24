package taboolib.expansion

import taboolib.common.reflect.Reflex.Companion.invokeMethod
import taboolib.module.database.Host
import taboolib.module.database.Table

/**
 * Container
 *
 * @author 坏黑
 * @constructor Create empty Container
 */

abstract class Container {

    abstract val host: Host<*>

    val hostTables = HashMap<String, Table<*, *>>()
    val hostTableOperator = HashMap<String, IContainerOperator<*>>()
    val dataSource by lazy(LazyThreadSafetyMode.NONE) { host.createDataSource(autoRelease = false) }

    abstract fun createTable(
        table: String,
        player: Boolean,
        playerKey: Boolean,
        data: List<ContainerBuilder.Data>
    ): Table<*, *>

    /**
     * 添加数据表
     */
    open fun addTable(table: String, player: Boolean, playerKey: Boolean, data: List<ContainerBuilder.Data>) {
        val containerTable = createTable(table, player, playerKey, data).also { hostTables[table] = it }
        // 扁平容器
        if (player && !playerKey && data.size == 2) {
            hostTableOperator[table] = ContainerOperatorFlatten(containerTable, dataSource, data[0].name, data[1].name)
        } else if (player && playerKey) {
            hostTableOperator[table] = ContainerOperatorString(containerTable, dataSource, data)
        } else {
            hostTableOperator[table] = ContainerOperatorNormal(containerTable, dataSource, player, data)
        }
    }

    open fun init() {
        hostTables.forEach { it.value.createTable(dataSource) }
    }

    open fun path(): String {
        return host.connectionUrl.toString()
    }

    open fun close() {
        dataSource.invokeMethod<Void>("close")
    }

    open fun operator(table: String): IContainerOperator<*> {
        return hostTableOperator[table] ?: error("Table not found")
    }
}