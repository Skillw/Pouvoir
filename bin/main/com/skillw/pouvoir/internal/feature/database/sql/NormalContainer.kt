package com.skillw.pouvoir.internal.feature.database.sql

import com.skillw.pouvoir.api.feature.database.BaseContainer
import com.skillw.pouvoir.api.feature.database.ContainerHolder
import com.skillw.pouvoir.api.feature.database.sql.IPouTable
import taboolib.module.database.ColumnBuilder
import taboolib.module.database.Host

/**
 * @className NormalContainer
 *
 * @author Glom
 * @date 2023/1/12 20:51 Copyright 2024 Glom. 
 */
open class NormalContainer<T : Host<E>, E : ColumnBuilder>(
    holder: ContainerHolder<NormalContainer<T, E>>,
    table: IPouTable<T, E>,
) : BaseContainer(table.name, holder), IPouTable<T, E> by table {
    override fun onDisable() {
        close()
    }
}