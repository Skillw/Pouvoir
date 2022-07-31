package com.skillw.pouvoir.util

import com.skillw.pouvoir.api.map.BaseMap
import java.util.*

/**
 * ClassName : com.skillw.pouvoir.util.MapUtils
 * Created by Glom_ on 2021-03-28 21:59:37
 * Copyright  2021 user. All rights reserved.
 */
object MapUtils {
    @JvmName("addSingleKListV")
    @JvmStatic
    fun <K : Any, V> BaseMap<K, LinkedList<V>>.put(key: K, value: V): BaseMap<K, LinkedList<V>> {
        if (!containsKey(key)) {
            this[key] = LinkedList(listOf(value))
        } else {
            this[key]!!.add(value)
        }
        return this
    }

    @JvmName("addSingleKSetV")
    @JvmStatic
    fun <K : Any, V> BaseMap<K, HashSet<V>>.put(key: K, value: V): BaseMap<K, HashSet<V>> {
        if (!containsKey(key)) {
            this[key] = HashSet(listOf(value))
        } else {
            this[key]!!.add(value)
        }
        return this
    }

    @JvmStatic
    fun <K : Any, V : Any, Z : Any> BaseMap<K, BaseMap<Z, V>>.put(
        key1: K,
        key2: Z,
        value: V,
    ): BaseMap<K, BaseMap<Z, V>> {
        if (!containsKey(key1)) {
            val map1: BaseMap<Z, V> = BaseMap()
            map1[key2] = value
            this[key1] = map1
        } else {
            this[key1]!![key2] = value
        }
        return this
    }

    @JvmName("putKZListV")
    fun <K : Any, V, Z : Any> BaseMap<K, BaseMap<Z, LinkedList<V>>>.put(
        key1: K,
        key2: Z,
        value: V,
    ): BaseMap<K, BaseMap<Z, LinkedList<V>>> {
        if (!containsKey(key1)) put(key1, BaseMap())
        get(key1)!!.put(key2, value)
        return this
    }

    @JvmStatic
    fun <K : Any, Z, V> BaseMap<K, Map<Z, V>>.getAllValues(): List<V> {
        val list = LinkedList<V>()
        forEach {
            list.addAll(it.value.values)
        }
        return list
    }
}