package com.skillw.pouvoir.internal.feature.database.sql

import com.skillw.pouvoir.api.feature.database.sql.IPouTable
import com.zaxxer.hikari.HikariDataSource
import taboolib.module.database.*
import javax.sql.DataSource


/**
 * @className PouTable
 *
 * @author Glom
 * @date 2023/1/12 20:12 Copyright 2023 user. All rights reserved.
 */
class SQLTable<T : Host<E>, E : ColumnBuilder>(private val table: Table<T, E>) : IPouTable<T, E> {
    override val name: String = table.name
    override val columns = table.columns
    override val primaryKeyForLegacy = table.primaryKeyForLegacy
    override val dataSource: DataSource = table.host.createDataSource(false)

    constructor(name: String, host: Host<E>) : this(Table(name, host))

    override fun column(name: String?, func: E.() -> Unit): Table<T, E> {
        return table.add(name, func)
    }

    override fun select(func: ActionSelect.() -> Unit): QueryTask {
        return table.workspace(dataSource) { select(func) }
    }

    override fun find(func: ActionSelect.() -> Unit): Boolean {
        return table.workspace(dataSource) { select(func) }.find()
    }

    override fun update(func: ActionUpdate.() -> Unit): Int {
        return table.workspace(dataSource) { update(func) }.run()
    }

    override fun delete(func: ActionDelete.() -> Unit): Int {
        return table.workspace(dataSource) { delete(func) }.run()
    }

    override fun insert(vararg keys: String, func: ActionInsert.() -> Unit): Int {
        return table.workspace(dataSource) { insert(*keys) { func(this) } }.run()
    }

    override fun workspace(func: Query.() -> Unit): QueryTask {
        return table.workspace(dataSource, func)
    }

    override fun createTable() {
        table.createTable(dataSource)
    }

    override fun close() {
        (dataSource as HikariDataSource).close()
    }
}
