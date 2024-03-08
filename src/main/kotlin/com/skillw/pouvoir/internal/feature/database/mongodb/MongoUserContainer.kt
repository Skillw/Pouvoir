package com.skillw.pouvoir.internal.feature.database.mongodb

import com.mongodb.client.MongoDatabase
import com.skillw.pouvoir.api.feature.database.UserBased
import org.bson.BsonDocument

/**
 * @className MongoContainer
 *
 * @author Glom
 * @date 2023/8/11 23:00 Copyright 2024 Glom.
 */
class MongoUserContainer(key: String, holder: MongoContainerHolder, database: MongoDatabase) :
    MongoContainer(key, holder, database), UserBased {

    override fun get(user: String, key: String): String? {
        val bson = BsonDocument.parse("{ user:$user }")
        val result = collection.find(bson)
        return result.first()?.get(key)?.toString()
    }

    override fun delete(user: String, key: String) {
        val bson = BsonDocument.parse("{ user:$user }")
        collection.deleteOne(bson)
    }

    override fun set(user: String, key: String, value: String?) {
        if (value == null) {
            delete(user, key)
            return
        }
        val target = BsonDocument.parse("{ user:$user }")
        val change = BsonDocument.parse("{ $key: $value }")
        collection.findOneAndUpdate(target, change)
    }

    override fun contains(user: String, key: String): Boolean {
        return get(user, key) != null
    }
}