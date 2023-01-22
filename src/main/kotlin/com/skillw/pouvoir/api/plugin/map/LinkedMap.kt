package com.skillw.pouvoir.api.plugin.map

import java.util.concurrent.CopyOnWriteArrayList

/**
 * Linked map
 *
 * @param K
 * @param V
 * @constructor Create empty Linked map
 */
open class LinkedMap<K : Any, V : Any> : BaseMap<K, V>() {
    /** List */
    protected val list = CopyOnWriteArrayList<K>()

    override fun put(key: K, value: V): V? {
        list.add(key)
        return super.put(key, value)
    }
}