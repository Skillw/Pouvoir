package com.skillw.pouvoir.internal.feature.database.redis

import com.skillw.pouvoir.api.feature.database.BaseContainer
import taboolib.expansion.SingleRedisConnection

/**
 * @className RedisContainer
 *
 * @author Glom
 * @date 2023/1/12 23:00 Copyright 2023 user. All rights reserved.
 */
open class RedisContainer(
    override val key: String,
    holder: RedisContainerHolder,
    val connection: SingleRedisConnection,
) : BaseContainer(key, holder)