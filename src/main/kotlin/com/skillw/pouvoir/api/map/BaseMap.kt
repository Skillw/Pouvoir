package com.skillw.pouvoir.api.map

import java.util.concurrent.ConcurrentHashMap

/**
 * ClassName : com.skillw.classsystem.api.map.BaseMap
 * Created by Glom_ on 2021-03-25 20:05:44
 * Copyright  2021 user. All rights reserved.
 */
open class BaseMap<K, V> : MutableMap<K, V> {
    protected var map = ConcurrentHashMap<K, V>()
    open fun register(key: K, value: V) {
        put(key, value)
    }

    override fun clear() {
        map.clear()
    }

    override fun containsKey(key: K): Boolean {
        return map.containsKey(key)
    }

    override fun containsValue(value: V): Boolean {
        return map.containsValue(value)
    }

    override fun put(key: K, value: V): V {
        map[key] = value
        return value
    }

    override operator fun get(key: K): V? {
        return map[key]
    }

    override fun putAll(from: Map<out K, V>) {
        map.putAll(from)
    }

    operator fun set(key: K, value: V) {
        put(key, value)
    }

    override fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    override val size: Int
        get() = map.size
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = map.entries
    final override val values: MutableCollection<V>
        get() = map.values

    override fun remove(key: K): V? {
        if (key != null)
            return map.remove(key)
        return null
    }

    override val keys: MutableSet<K>
        get() = map.keys
}