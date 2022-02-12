package com.skillw.pouvoir.util

import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * ClassName : com.skillw.com.skillw.rpglib.util.MapUtils
 * Created by Glom_ on 2021-03-28 21:59:37
 * Copyright  2021 user. All rights reserved.
 */
object MapUtils {
    @JvmName("addSingleList")
    @JvmStatic
    fun <K, V> MutableMap<K, LinkedList<V>>.addSingle(key: K, value: V): Map<K, LinkedList<V>> {
        return add(this, key, value)
    }

    @JvmStatic
    fun <K, V> add(map: MutableMap<K, LinkedList<V>>, key: K, value: V): Map<K, LinkedList<V>> {
        if (!map.containsKey(key)) {
            map[key] = LinkedList(listOf(value))
        } else {
            map[key]!!.add(value)
        }
        return map
    }

    @JvmName("addSingleKV")
    @JvmStatic
    fun <K, V> MutableMap<K, HashSet<V>>.addSingle(key: K, value: V): Map<K, HashSet<V>> {
        return add(this, key, value)
    }


    @JvmName("addSingleSet")
    @JvmStatic
    fun <K, V> add(map: MutableMap<K, HashSet<V>>, key: K, value: V): Map<K, HashSet<V>> {
        if (!map.containsKey(key)) {
            map[key] = HashSet(listOf(value))
        } else {
            map[key]!!.add(value)
        }
        return map
    }

    @JvmStatic
    fun <K, V, Z> MutableMap<K, MutableMap<Z, V>>.putFast(key1: K, key2: Z, value: V): Map<K, MutableMap<Z, V>> {
        return MapUtils.put(this, key1, key2, value)
    }

    @JvmStatic
    fun <K, V, Z> put(map: MutableMap<K, MutableMap<Z, V>>, key1: K, key2: Z, value: V): Map<K, MutableMap<Z, V>> {
        if (!map.containsKey(key1)) {
            val map1: MutableMap<Z, V> = ConcurrentHashMap()
            map1[key2] = value
            map[key1] = map1
        } else {
            map[key1]!![key2] = value
        }
        return map
    }

    @JvmStatic
    fun <K, Z, V> Map<K, Map<Z, V>>.getAllValues(): List<V> {
        return getValues(this)
    }

    @JvmStatic
    fun <K, Z, V> getValues(map: Map<K, Map<Z, V>>): List<V> {
        val list: MutableList<V> = LinkedList()
        map.forEach { (_: K, v1: Map<Z, V>) -> v1.forEach { (_: Z, z: V) -> list.add(z) } }
        return list
    }
}