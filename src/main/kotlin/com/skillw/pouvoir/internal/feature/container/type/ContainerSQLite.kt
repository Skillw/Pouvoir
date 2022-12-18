package com.skillw.pouvoir.internal.feature.container.type

import com.skillw.pouvoir.api.container.Container
import com.skillw.pouvoir.api.container.ContainerBuilder
import taboolib.module.database.ColumnOptionSQLite
import taboolib.module.database.ColumnTypeSQLite
import taboolib.module.database.HostSQLite
import taboolib.module.database.Table
import java.io.File

// 来自TLib6
class ContainerSQLite(file: File) : Container() {

    override val host = HostSQLite(file)

    override fun createTable(
        table: String,
        player: Boolean,
        playerKey: Boolean,
        data: List<ContainerBuilder.Data>,
    ): Table<*, *> {
        return Table(table, host) {
            // 玩家容器
            if (player) {
                add("username") {
                    type(ColumnTypeSQLite.TEXT, 36) {
                        if (playerKey && !player) {
                            options(ColumnOptionSQLite.PRIMARY_KEY)
                        }
                    }
                }
            }
            data.forEach {
                add(it.name) {
                    val type = when {
                        it.int || it.long -> ColumnTypeSQLite.INTEGER
                        it.double -> ColumnTypeSQLite.NUMERIC
                        else -> ColumnTypeSQLite.TEXT
                    }
                    type(type, if (type == ColumnTypeSQLite.TEXT) it.length else 0)
                }
            }
        }
    }
}