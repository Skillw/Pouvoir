package com.skillw.pouvoir.internal.feature.database.redis
//
//import com.skillw.pouvoir.api.feature.database.UserBased
//import taboolib.expansion.SingleRedisConnection
//
///**
// * @className MongoContainer
// *
// * @author Glom
// * @date 2023/1/12 23:00 Copyright 2024 Glom.
// */
//class RedisUserContainer(key: String, holder: RedisContainerHolder, connection: SingleRedisConnection) :
//    RedisContainer(key, holder, connection), UserBased {
//    override fun get(user: String, key: String): String? {
//        return connection["$user-$key"]
//    }
//
//    override fun delete(user: String, key: String) {
//        connection.delete(key)
//    }
//
//    override fun set(user: String, key: String, value: String?) {
//        connection["$user-$key"] = value
//    }
//
//    override fun contains(user: String, key: String): Boolean {
//        return connection.contains("$user-$key")
//    }
//}