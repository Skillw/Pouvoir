package com.skillw.pouvoir.internal.feature.database.sql

import com.skillw.pouvoir.api.feature.database.UserBased
import com.skillw.pouvoir.api.feature.database.sql.IPouTable
import taboolib.module.database.ColumnBuilder
import taboolib.module.database.Host
import java.util.concurrent.ConcurrentHashMap

/**
 * @className UserDatabase
 *
 * @author Glom
 * @date 2023/1/20 16:58 Copyright 2023 user. All rights reserved.
 */
class UserDatabase<T : Host<E>, E : ColumnBuilder>(table: IPouTable<T, E>) : IPouTable<T, E> by table, UserBased {
    fun saveAll(cache: Map<String, Map<String, String>>) {
        cache.forEach { (user, map) ->
            map.forEach { (key, value) ->
                this[user, key] = value
            }
        }
    }

    fun loadAll(cache: ConcurrentHashMap<String, ConcurrentHashMap<String, String>>) {
        select {}.forEach {
            val user = getString("username")
            val key = getString("key")
            val value = getString("value")
            cache.computeIfAbsent(user) { ConcurrentHashMap() }[key] = value
        }
    }

    override fun get(user: String, key: String): String? {
        return select {
            where("username" eq user and ("key" eq key))
        }.firstOrNull {
            getString("value")
        }
    }

    override fun delete(user: String, key: String) {
        delete {
            where("username" eq user and ("key" eq key))
        }
    }

    override fun set(user: String, key: String, value: String?) {
        value ?: delete(user, key)
        if (contains(user, key)) {
            update {
                where("username" eq user and ("key" eq key))
                set("value", value)
            }
        } else {
            insert("username", "key", "value") {
                value(user, key, value ?: return@insert)
            }
        }
    }

    override fun contains(user: String, key: String): Boolean {
        return find { where("username" eq user and ("key" eq key)) }
    }
}