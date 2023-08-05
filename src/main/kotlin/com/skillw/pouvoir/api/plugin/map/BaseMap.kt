package com.skillw.pouvoir.api.plugin.map

import java.util.*
import java.util.concurrent.ConcurrentHashMap


/**
 * Base map
 *
 * @param K
 * @param V
 * @constructor Create empty Base map
 */
open class BaseMap<K : Any, V : Any> : ConcurrentHashMap<K, V>(), RegContainer<K, V> {

    override operator fun get(key: K): V? {
        return super.get(key)
    }

    private val uuid = UUID.randomUUID()
    override fun register(key: K, value: V): V? {
        return put(key, value)
    }


    override fun remove(key: K, value: V): Boolean {
        return super<ConcurrentHashMap>.remove(key, value)
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseMap<*, *>) return false
        return uuid == other.uuid
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }
}
