package com.skillw.pouvoir.internal.feature.database.redis

import com.skillw.pouvoir.api.feature.database.ContainerHolder
import taboolib.expansion.SingleRedisConnection

/**
 * @className MongoContainerHolder
 *
 * @author Glom
 * @date 2023/1/20 17:29 Copyright 2024 Glom.
 */
class RedisContainerHolder(val connection: SingleRedisConnection) : ContainerHolder<RedisContainer>() {
    override fun createContainer(tableName: String, userKey: Boolean): RedisContainer =
        if (!userKey) RedisContainer(tableName, this, connection)
        else RedisUserContainer(tableName, this, connection)

    override fun disconnect() {
        connection.close()
    }

}