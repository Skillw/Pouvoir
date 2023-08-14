@file:RuntimeDependencies(
    RuntimeDependency(value = "!com.google.code.gson:gson:2.8.7", test = "!com.google.gson.JsonElement"),
    RuntimeDependency(
        value = "!com.mongodb:MongoDB:3.12.2",
        test = "!com.mongodb.client.MongoClient",
        repository = "http://ptms.ink:8081/repository/releases"
    )
)

package com.skillw.pouvoir.internal.feature.database.mongodb

import com.mongodb.ConnectionString
import com.mongodb.client.MongoClients
import com.skillw.pouvoir.api.feature.database.DatabaseType
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.plugin.map.DataMap
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency

/**
 * MongoDB
 *
 * ContainerHolder -> database Container -> collection
 *
 * @constructor Create empty Mongo d b
 */

@AutoRegister
internal object MongoDB : DatabaseType<MongoContainerHolder, MongoContainer>("mongoDB") {
    override fun connect(params: DataMap): MongoContainerHolder {
        val client = params["client"].toString()
        val database = params["database"].toString()

        val mongoClient = MongoClients.create(ConnectionString(client))
        return MongoContainerHolder(mongoClient, database)
    }
}