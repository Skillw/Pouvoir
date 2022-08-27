package com.skillw.pouvoir.internal.feature.container.operator

import com.skillw.pouvoir.api.container.ContainerBuilder
import com.skillw.pouvoir.api.container.IContainerOperator
import taboolib.module.database.Table
import taboolib.module.database.Where
import javax.sql.DataSource

class ContainerOperatorNormal(
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
            where("key" eq unique)
            limit(1)
        }.firstOrNull { data.associate { it.name to getObject(it.name) } } ?: emptyMap()
    }

    override fun get(unique: String, vararg rows: String): Map<String, Any?> {

        return table.select(dataSource) {
            rows(*rows)
            where("key" eq unique)
            limit(1)
        }.firstOrNull { rows.associateWith { getObject(it) } } ?: emptyMap()
    }

    override fun set(unique: String, map: Map<String, Any?>) {
        if (table.find(dataSource) { where("key" eq unique) }) {
            table.update(dataSource) {
                where("key" eq unique)
                map.forEach { (k, v) -> set(k, v) }
            }
        } else {
            val newMap = map.filterValues { it != null }.map { it.key to it.value!! }.toMap()
            table.insert(dataSource, "key", *newMap.keys.toTypedArray()) {
                value(unique, *newMap.values.toTypedArray())
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