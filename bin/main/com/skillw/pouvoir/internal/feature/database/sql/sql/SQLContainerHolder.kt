package com.skillw.pouvoir.internal.feature.database.sql.sql

import com.skillw.pouvoir.api.feature.database.ContainerHolder
import com.skillw.pouvoir.internal.feature.database.sql.NormalContainer
import com.skillw.pouvoir.internal.feature.database.sql.SQLTable
import com.skillw.pouvoir.internal.feature.database.sql.UserContainer
import taboolib.module.database.ColumnOptionSQL
import taboolib.module.database.ColumnTypeSQL
import taboolib.module.database.Host
import taboolib.module.database.SQL

/**
 * @className SQLContainerHolder
 *
 * @author Glom
 * @date 2023/1/20 17:42 Copyright 2023 user. All rights reserved.
 */
class SQLContainerHolder(val host: Host<SQL>) : ContainerHolder<NormalContainer<Host<SQL>, SQL>>() {
    override fun createContainer(
        tableName: String,
        userKey: Boolean,
    ): NormalContainer<Host<SQL>, SQL> {
        val table = SQLTable(tableName, host)
        if (userKey) {
            table.apply {
                column("username") {
                    type(ColumnTypeSQL.VARCHAR) {
                        options(ColumnOptionSQL.UNIQUE_KEY)
                    }
                }
                column("key") {
                    type(ColumnTypeSQL.VARCHAR) {
                        options(ColumnOptionSQL.UNIQUE_KEY)
                    }
                }
                column("value") {
                    type(ColumnTypeSQL.VARCHAR)
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