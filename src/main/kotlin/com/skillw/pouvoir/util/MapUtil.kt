package com.skillw.pouvoir.util

import com.skillw.pouvoir.api.plugin.map.BaseMap
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.*

/**
 * ClassName : com.skillw.pouvoir.feature.MapUtils Created by Glom_ on
 * 2021-03-28 21:59:37 Copyright 2021 user. All rights reserved.
 */

@JvmName("addSingleKListV")

fun <K : Any, V> BaseMap<K, LinkedList<V>>.put(key: K, value: V): BaseMap<K, LinkedList<V>> {
    if (!containsKey(key)) {
        this[key] = LinkedList(listOf(value))
    } else {
        this[key]!!.add(value)
    }
    return this
}

@JvmName("addSingleKSetV")

fun <K : Any, V> BaseMap<K, HashSet<V>>.put(key: K, value: V): BaseMap<K, HashSet<V>> {
    if (!containsKey(key)) {
        this[key] = HashSet(listOf(value))
    } else {
        this[key]!!.add(value)
    }
    return this
}

@JvmName("addSingleKSetV2")

fun <K : Any, V> BaseMap<K, LinkedHashSet<V>>.put(key: K, value: V): BaseMap<K, LinkedHashSet<V>> {
    if (!containsKey(key)) {
        this[key] = LinkedHashSet(listOf(value))
    } else {
        this[key]!!.add(value)
    }
    return this
}


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


fun <K : Any, Z, V> BaseMap<K, Map<Z, V>>.getAllValues(): List<V> {
    val list = ArrayList<V>()
    forEach {
        list.addAll(it.value.values)
    }
    return list
}


fun MutableMap<String, Any>.putEntity(entity: Entity?) {
    entity?.let {
        put("entity", entity)
        if (it is Player) {
            put("player", it)
        }
    }
}


fun MutableMap<String, Any>.putDeep(key: String, value: Any): Any? {
    var map: MutableMap<String, Any>? = this
    var list: MutableList<Any>? = null
    val keys = key.split(".")
    val lastIndex = keys.lastIndex
    for (i in keys.indices) {
        val keyStr = keys[i]

        if (i == lastIndex) {
            map?.put(keyStr, value) ?: list?.set(keyStr.toInt(), value)
            break
        }

        when (val obj = map?.get(keyStr) ?: keyStr.toIntOrNull()?.let { list?.getOrNull(it) }) {
            is Map<*, *> -> {
                map = obj as MutableMap<String, Any>
                list = null
            }

            is List<*> -> {
                list = obj as MutableList<Any>?
                map = null
            }

            null -> {
                map?.let {
                    HashMap<String, Any>().also { newMap ->
                        it[keyStr] = newMap
                        map = newMap
                    }
                }
                list?.let {
                    val index = keyStr.toInt()
                    ArrayList<Any>().also { newList ->
                        it[index] = newList
                        list = newList
                    }
                }
            }

            else -> {
                return null
            }
        }
    }
    return null
}


fun MutableMap<String, Any>.getDeep(key: String): Any? {
    var map: MutableMap<String, Any>? = this
    var list: MutableList<Any>? = null
    val keys = key.split(".")
    val lastIndex = keys.lastIndex
    for (i in keys.indices) {
        val keyStr = keys[i]
        val obj = map?.get(keyStr) ?: keyStr.toIntOrNull()?.let { list?.getOrNull(it) }
        if (i == lastIndex) return obj
        when (obj) {
            is Map<*, *> -> {
                map = obj as MutableMap<String, Any>
                list = null
            }

            is List<*> -> {
                list = obj as MutableList<Any>?
                map = null
            }

            else -> {
                return null
            }
        }
    }
    return null
}


internal fun <T : Any> T.clone(): Any {
    return when (this) {
        is Map<*, *> -> {
            val map = HashMap<String, Any>()
            forEach { (key, value) ->
                key ?: return@forEach
                value ?: return@forEach
                map[key.toString()] = value.clone()
            }
            map
        }

        is List<*> -> {
            val list = ArrayList<Any>()
            mapNotNull { it }.forEach {
                list.add(it.clone())
            }
            list
        }

        else -> this
    }
}