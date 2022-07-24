package taboolib.expansion

import taboolib.module.database.Table
import taboolib.module.database.Where
import java.util.*
import javax.sql.DataSource

class ContainerOperatorNormal(
    val table: Table<*, *>,
    val dataSource: DataSource,
    val player: Boolean,
    val data: List<ContainerBuilder.Data>
) :
    IContainerOperator<UUID> {

    override fun keys(unique: UUID): List<String> {
        error("Not supported in normal container")
    }

    override fun get(unique: UUID): Map<String, Any?> {
        return if (player) {
            table.select(dataSource) {
                rows(*data.map { it.name }.toTypedArray())
                where("username" eq unique.toString())
                limit(1)
            }.firstOrNull { data.associate { it.name to getObject(it.name) } } ?: emptyMap()
        } else {
            error("Not supported in no-player container")
        }
    }

    override fun get(unique: UUID, vararg rows: String): Map<String, Any?> {
        return if (player) {
            table.select(dataSource) {
                rows(*rows)
                where("username" eq unique.toString())
                limit(1)
            }.firstOrNull { rows.associateWith { getObject(it) } } ?: emptyMap()
        } else {
            error("Not supported in no-player container")
        }
    }

    override fun set(unique: UUID, map: Map<String, Any?>) {
        if (player) {
            if (table.find(dataSource) { where("username" eq unique.toString()) }) {
                table.update(dataSource) {
                    where("username" eq unique.toString())
                    map.forEach { (k, v) -> set(k, v) }
                }
            } else {
                val newMap = map.filterValues { it != null }.map { it.key to it.value!! }.toMap()
                table.insert(dataSource, "username", *newMap.keys.toTypedArray()) {
                    value(unique.toString(), *newMap.values.toTypedArray())
                }
            }
        } else {
            error("Not supported in no-player container")
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