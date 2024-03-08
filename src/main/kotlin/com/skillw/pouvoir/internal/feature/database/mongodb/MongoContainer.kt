package com.skillw.pouvoir.internal.feature.database.mongodb

import com.google.gson.Gson
import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.skillw.pouvoir.api.feature.database.BaseContainer
import org.bson.Document
import taboolib.module.configuration.util.asMap

/**
 * @className MongoContainer
 *
 * @author Glom
 * @date 2023/8/11 23:00 Copyright 2024 Glom.
 */
/**
 * MongoContainer -> Collection
 *
 * @constructor
 * @property key String Collection
 * @property database MongoDatabase
 */
open class MongoContainer(
    final override val key: String,
    holder: MongoContainerHolder,
    val database: MongoDatabase,
) : BaseContainer(key, holder) {
    protected val collection: MongoCollection<Document> = database.getCollection(key)
    protected val gson = Gson()

    fun insert(vararg objects: Any) {

        collection.insertMany(objects.map { Document().apply { putAll(gson.toJsonTree(it).asMap()) } })
    }

    fun delete(map: Map<String, Any?>): DeleteResult {
        val bson = Document().apply { putAll(gson.toJsonTree(map).asMap()) }
        return collection.deleteMany(bson)
    }

    fun update(target: Map<String, Any?>, new: Map<String, Any?>): UpdateResult {
        val toUpdate = Document().apply { putAll(target) }
        val changed = Document().apply { putAll(new) }
        return collection.updateMany(toUpdate, changed)
    }

    fun find(map: Map<String, Any?>): FindIterable<Document> {
        val bson = Document().apply { putAll(map.mapValues { it.value!! }) }
        return collection.find(bson)
    }


}