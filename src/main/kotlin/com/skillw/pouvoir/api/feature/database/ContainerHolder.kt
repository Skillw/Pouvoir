package com.skillw.pouvoir.api.feature.database

import com.skillw.pouvoir.api.plugin.map.LowerKeyMap

/**
 * @className ContainerHolder
 *
 * 容器持有者，负责管理一种特定类型的容器
 *
 * 包括此类型容器的创建 销毁
 *
 * @author Glom
 * @date 2023/1/20 17:26 Copyright 2023 user. All rights reserved.
 */
abstract class ContainerHolder<C : BaseContainer> : LowerKeyMap<C>() {
    var userContainerSyncTime: Long = 360000

    /**
     * Create table
     *
     * @param tableName
     * @return
     */
    protected abstract fun createContainer(tableName: String, userKey: Boolean = false): C
    fun container(tableName: String, userKey: Boolean = false): C {
        return computeIfAbsent(tableName) { this.createContainer(tableName, userKey).apply { onEnable() } }
    }

    protected abstract fun disconnect()
    fun disconnectAll() {
        values.forEach(BaseContainer::onDisable)
        disconnect()
    }
}