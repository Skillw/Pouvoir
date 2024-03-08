package com.skillw.pouvoir.internal.feature.database.sql.sqlite

import com.skillw.pouvoir.api.feature.database.ContainerHolder
import com.skillw.pouvoir.internal.feature.database.sql.NormalContainer
import com.skillw.pouvoir.internal.feature.database.sql.SQLTable
import com.skillw.pouvoir.internal.feature.database.sql.UserContainer
import taboolib.module.database.ColumnOptionSQLite
import taboolib.module.database.ColumnTypeSQLite
import taboolib.module.database.Host
import taboolib.module.database.SQLite

/**
 * @className SQLContainerHolder
 *
 * @author Glom
 * @date 2023/1/20 17:42 Copyright 2024 Glom.
 */
class SQLiteContainerHolder(val host: Host<SQLite>) : ContainerHolder<NormalContainer<Host<SQLite>, SQLite>>() {
    override fun createContainer(
        tableName: String,
        userKey: Boolean,
    ): NormalContainer<Host<SQLite>, SQLite> {
        val table = SQLTable(tableName, host)
        if (userKey) {
            table.apply {
                column("username") {
                    type(ColumnTypeSQLite.TEXT) {
                        options(ColumnOptionSQLite.PRIMARY_KEY)
                    }
                }
                column("key") {
                    type(ColumnTypeSQLite.TEXT) {
                        options(ColumnOptionSQLite.NOTNULL)
                    }
                }
                column("value") {
                    type(ColumnTypeSQLite.TEXT){
                        options(ColumnOptionSQLite.NOTNULL)
                    }
                }
            }
            return UserContainer(this, table).apply { createTable() }
        }
        return NormalContainer(this, table)
    }

    override fun disconnect() {
        //TLib会自动断联
    }

}