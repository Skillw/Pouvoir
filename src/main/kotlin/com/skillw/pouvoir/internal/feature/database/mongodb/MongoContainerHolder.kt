package com.skillw.pouvoir.internal.feature.database.mongodb

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import com.skillw.pouvoir.api.feature.database.ContainerHolder

/**
 * @className MongoContainerHolder
 *
 * @author Glom
 * @date 2023/8/11 22:49 Copyright 2023 user. All rights reserved.
 */
class MongoContainerHolder(private val mongoClient: MongoClient, database: String) : ContainerHolder<MongoContainer>() {
    private val mongoDatabase: MongoDatabase = mongoClient.getDatabase(database)

    override fun createContainer(tableName: String, userKey: Boolean): MongoContainer {
        return MongoContainer(tableName, this, mongoDatabase)
    }

    override fun disconnect() {
        mongoClient.close()
    }

}