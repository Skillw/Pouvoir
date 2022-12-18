package com.skillw.pouvoir.internal.feature.container.operator

import com.skillw.pouvoir.api.container.ContainerBuilder
import com.skillw.pouvoir.api.container.IContainerOperator
import taboolib.module.database.Table
import taboolib.module.database.Where
import javax.sql.DataSource
// 来自TLib6
class ContainerOperatorString(
    val table: Table<*, *>,
    val dataSource: DataSource,
    val data: List<ContainerBuilder.Data>,
) :
    IContainerOperator<String> {

    override fun keys(unique: String): List<String> {
        error("Not supported in normal container")
    }

    override fun get(unique: String): Map<String, Any?> {
        return table.select(dataSource) {
            rows(*data.map { it.name }.toTypedArray())
            where("username" eq unique)
        }.run {
            val map = HashMap<String, Any>()
            forEach {
                map[getString("key")] = getString("value")
            }
            map
        }
    }

    override fun get(unique: String, vararg rows: String): Map<String, Any?> {
        error("Not supported in player container")
    }

    override fun set(unique: String, map: Map<String, Any?>) {
        val key = map["key"] ?: return
        val value = map["value"]

        if (table.find(dataSource) { where("username" eq unique and ("key" eq key)) }) {
            table.update(dataSource) {
                where("username" eq unique and ("key" eq key))
                set("value", value)
            }
        } else {
            table.insert(dataSource, "username", "key", "value") {
                value(unique, key, value ?: return@insert)
            }
        }
    }

    override fun select(where: Where.() -> Unit): Map<String, Any?> {
        return table.select(dataSource) {
            rows(*data.map { it.name }.toTypedArray())
            where(where)
            limit(1)
        }.firstOrNull { data.associate { it.name to getObject(it.name) } } ?: emptyMap()
    }

    override fun select(vararg rows: String, where: Where.() -> Unit): Map<String, Any?> {
        return table.select(dataSource) {
            rows(*rows)
            where(where)
            limit(1)
        }.firstOrNull { rows.associateWith { getObject(it) } } ?: emptyMap()
    }

    override fun update(map: Map<String, Any?>, where: Where.() -> Unit) {
        if (table.find(dataSource) { where(where) }) {
            table.update(dataSource) {
                where(where)
                map.forEach { (k, v) -> set(k, v) }
            }
        } else {
            table.insert(dataSource, *map.keys.toTypedArray()) {
                value(map.values.toTypedArray())
            }
        }
    }
}