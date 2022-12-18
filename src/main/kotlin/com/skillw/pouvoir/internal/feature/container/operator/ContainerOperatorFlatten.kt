package com.skillw.pouvoir.internal.feature.container.operator

import com.skillw.pouvoir.api.container.IContainerOperator
import taboolib.module.database.Table
import taboolib.module.database.Where
import java.util.*
import javax.sql.DataSource
// 来自TLib6
class ContainerOperatorFlatten(
    val table: Table<*, *>,
    val dataSource: DataSource,
    val key: String,
    val value: String,
) : IContainerOperator<UUID> {

    override fun keys(unique: UUID): List<String> {
        return table.select(dataSource) {
            rows(key)
            where("username" eq unique.toString())
        }.map { getString(key) }.toList()
    }

    override fun get(unique: UUID): Map<String, Any?> {
        return table.select(dataSource) {
            rows(key, value)
            where("username" eq unique.toString())
        }.map { getString(key) to getObject(value) }.toMap()
    }

    override fun get(unique: UUID, vararg rows: String): Map<String, Any?> {
        return table.select(dataSource) {
            rows(key, value)
            where("username" eq unique.toString() and (key inside arrayOf(*rows)))
        }.map { getString(key) to getObject(value) }.toMap()
    }

    override fun set(unique: UUID, map: Map<String, Any?>) {
        val keys = keys(unique)
        val updateMap = map.filterKeys { it in keys }
        val insertMap = map.filterKeys { it !in keys }
        // 更新数据
        updateMap.forEach { (k, v) ->
            table.update(dataSource) {
                where("username" eq unique.toString() and (key eq k))
                set(value, v)
            }
        }
        // 插入数据
        if (insertMap.isNotEmpty()) {
            table.insert(dataSource, "username", key, value) {
                insertMap.filter { it.value != null }.forEach { (k, v) -> value(unique.toString(), k, v!!) }
            }
        }
    }

    override fun select(where: Where.() -> Unit): Map<String, Any?> {
        error("Not supported in flatten container")
    }

    override fun select(vararg rows: String, where: Where.() -> Unit): Map<String, Any?> {
        error("Not supported in flatten container")
    }

    override fun update(map: Map<String, Any?>, where: Where.() -> Unit) {
        error("Not supported in flatten container")
    }
}