package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.feature.database.ContainerHolder
import com.skillw.pouvoir.api.feature.database.DatabaseType
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.map.DataMap
import com.skillw.pouvoir.api.plugin.map.LowerKeyMap

/**
 * @className DatabaseManager
 *
 * 数据库管理器 主要负责维护数据库类型
 *
 * @author Glom
 * @date 2023/1/12 18:57 Copyright 2023 user. All rights reserved.
 */
abstract class DatabaseManager : Manager, LowerKeyMap<DatabaseType<*, *>>() {
    /**
     * 创建容器持有者
     *
     * @param data 参数
     * @return 容器持有者
     */
    abstract fun containerHolder(data: DataMap): ContainerHolder<*>?
}