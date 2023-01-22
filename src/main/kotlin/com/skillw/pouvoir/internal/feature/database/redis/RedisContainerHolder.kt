package com.skillw.pouvoir.internal.feature.database.redis

import com.skillw.pouvoir.api.feature.database.ContainerHolder
import taboolib.expansion.SingleRedisConnection

/**
 * @className RedisContainerHolder
 *
 * @author Glom
 * @date 2023/1/20 17:29 Copyright 2023 user. All rights reserved.
 */
class RedisContainerHolder(val connection: SingleRedisConnection) : ContainerHolder<RedisContainer>() {
    override fun createContainer(tableName: String, userKey: Boolean): RedisContainer {
        return RedisContainer(tableName, this, connection)
    }

    override fun disconnect() {
        connection.close()
    }

}