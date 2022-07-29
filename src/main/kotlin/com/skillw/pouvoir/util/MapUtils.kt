package com.skillw.pouvoir.util

import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * ClassName : com.skillw.pouvoir.util.MapUtils
 * Created by Glom_ on 2021-03-28 21:59:37
 * Copyright  2021 user. All rights reserved.
 */
object MapUtils {
    @JvmName("addSingleKListV")
    @JvmStatic
    fun <K, V> MutableMap<K, LinkedList<V>>.put(key: K, value: V): Map<K, LinkedList<V>> {
        if (!containsKey(key)) {
            this[key] = LinkedList(listOf(value))
        } else {
            this[key]!!.add(value)
        }
        return this
    }

    @JvmName("addSingleKSetV")
    @JvmStatic
    fun <K, V> MutableMap<K, HashSet<V>>.put(key: K, value: V): Map<K, HashSet<V>> {
        if (!containsKey(key)) {
            this[key] = HashSet(listOf(value))
        } else {
            this[key]!!.add(value)
        }
        return this
    }

    @JvmStatic
    fun <K, V, Z> MutableMap<K, MutableMap<Z, V>>.put(key1: K, key2: Z, value: V): MutableMap<K, MutableMap<Z, V>> {
        if (!containsKey(key1)) {
            val map1: MutableMap<Z, V> = ConcurrentHashMap()
            map1[key2] = value
            this[key1] = map1
        } else {
            this[key1]!![key2] = value
        }
        return this
    }

    @JvmName("putKZListV")
    fun <K, V, Z> MutableMap<K, MutableMap<Z, LinkedList<V>>>.put(
        key1: K,
        key2: Z,
        value: V
    ): MutableMap<K, MutableMap<Z, LinkedList<V>>> {
        if (!containsKey(key1)) put(key1, ConcurrentHashMap())
        get(key1)!!.put(key2, value)
        return this
    }

    @JvmStatic
    fun <K, Z, V> Map<K, Map<Z, V>>.getAllValues(): List<V> {
        val list = LinkedList<V>()
        forEach {
            list.addAll(it.value.values)
        }
        return list
    }
}