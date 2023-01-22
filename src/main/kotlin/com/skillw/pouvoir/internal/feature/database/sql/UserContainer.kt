package com.skillw.pouvoir.internal.feature.database.sql

import com.skillw.pouvoir.api.feature.database.ContainerHolder
import com.skillw.pouvoir.api.feature.database.UserBased
import com.skillw.pouvoir.api.feature.database.sql.IPouTable
import taboolib.common.platform.function.submitAsync
import taboolib.module.database.ColumnBuilder
import taboolib.module.database.Host
import java.util.concurrent.ConcurrentHashMap

/**
 * @className NormalContainer
 *
 * @author Glom
 * @date 2023/1/12 20:51 Copyright 2023 user. All rights reserved.
 */
class UserContainer<T : Host<E>, E : ColumnBuilder>(
    holder: ContainerHolder<NormalContainer<T, E>>,
    table: IPouTable<T, E>,
) : NormalContainer<T, E>(holder, table), UserBased {
    private val cache = ConcurrentHashMap<String, ConcurrentHashMap<String, String>>()
    private val database by lazy {
        UserDatabase(this)
    }

    override fun database(): UserDatabase<T, E> {
        return database
    }

    override fun onEnable() {
        database.loadAll(cache)
        submitAsync(period = holder.userContainerSyncTime) {
            database.saveAll(cache)
        }
    }

    override fun onDisable() {
        cache.forEach { (user, map) ->
            map.forEach { (key, value) ->
                database[user, key] = value
            }
        }
        close()
    }

    override fun get(user: String, key: String): String? {
        return cache[user]?.get(key)
    }

    override fun delete(user: String, key: String) {
        cache[user]?.remove(key)
    }

    override fun set(user: String, key: String, value: String?) {
        cache.computeIfAbsent(user) { ConcurrentHashMap() }[key] = value ?: return
    }

    override fun contains(user: String, key: String): Boolean {
        return cache.containsKey(user) && cache[user]!!.containsKey(key)
    }
}