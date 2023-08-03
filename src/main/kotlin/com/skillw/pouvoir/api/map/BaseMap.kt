package com.skillw.pouvoir.api.map

import com.skillw.pouvoir.api.able.RegContainer
import java.util.concurrent.ConcurrentHashMap


/**
 * Base map
 *
 * @param K
 * @param V
 * @constructor Create empty Base map
 */
open class BaseMap<K : Any, V : Any> : ConcurrentHashMap<K, V>(), RegContainer<K, V> {
    /** Map */

    override fun register(key: K, value: V): V? {
        return put(key, value)
    }

    override fun remove(key: K, value: V): Boolean {
        return super<ConcurrentHashMap>.remove(key, value)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseMap<*, *>) return false
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }


}
