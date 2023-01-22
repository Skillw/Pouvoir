package com.skillw.pouvoir.internal.feature.database.redis

import com.skillw.pouvoir.api.feature.database.DatabaseType
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.plugin.map.DataMap
import taboolib.expansion.SingleRedisConnector

@AutoRegister
internal object Redis : DatabaseType<RedisContainerHolder, RedisContainer>("redis") {
    override fun connect(params: DataMap): RedisContainerHolder {
        val host = params.get("host", "localhost")
        val port = params.get("port", 6379)
        val auth = params.get("auth", "root")
        val connect = params.get("connect", 32)
        val timeout = params.get("timeout", 1000)
        return SingleRedisConnector()
            .host(host)
            .port(port)
            .auth(auth)
            .connect(connect)
            .timeout(timeout)
            .connect()
            .connection()
            .let { RedisContainerHolder(it) }
    }
}