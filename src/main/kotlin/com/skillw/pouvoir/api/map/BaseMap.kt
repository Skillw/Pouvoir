package com.skillw.rpglib.api.map

import java.util.concurrent.ConcurrentHashMap

/**
 * ClassName : com.skillw.classsystem.api.map.BaseMap
 * Created by Glom_ on 2021-03-25 20:05:44
 * Copyright  2021 user. All rights reserved.
 */
abstract class BaseMap<K, V> {
    protected var map = ConcurrentHashMap<K, V>()
    open fun register(k: K, v: V) {
        map[k] = v
    }

    open fun removeByKey(k: K) {
        map.remove(k)
    }

    open fun clear() {
        map.clear()
    }

    open val keys: Set<K>?
        get() = map.keys
    open val objects: Collection<V>?
        get() = map.values

    open fun size(): Int {
        return map.size
    }

    open fun entrySet(): Set<Map.Entry<K, V>?>? {
        return map.entries
    }

    open fun containsKey(k: K): Boolean {
        return map.containsKey(k)
    }

    open fun containsValue(v: V): Boolean {
        return map.containsValue(v)
    }

    open fun put(k: K, v: V) {
        register(k, v)
    }

    open fun hasKey(k: K): Boolean {
        return map.containsKey(k)
    }

    open fun hasObject(v: V): Boolean {
        return map.contains(v)
    }

    open operator fun get(k: K): V? {
        return map[k]
    }
    
    operator fun set(k: K, v: V) {
        put(k, v)
    }

    open val isEmpty: Boolean
        get() = map.isEmpty()
}