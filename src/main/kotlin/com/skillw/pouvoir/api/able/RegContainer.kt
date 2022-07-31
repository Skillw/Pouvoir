package com.skillw.pouvoir.api.able

/**
 * @className RegContainer
 * @author Glom
 * @date 2022/7/30 20:35
 * Copyright  2022 user. All rights reserved.
 */
interface RegContainer<K, V> {
    val size: Int
    val entries: MutableSet<MutableMap.MutableEntry<K, V>>
    val keys: MutableSet<K>
    val values: MutableCollection<V>
    fun clear()
    fun put(key: K, value: V): V?
    fun putAll(from: Map<out K, V>)
    fun remove(key: K): V?
    fun containsKey(key: K): Boolean
    fun containsValue(value: V): Boolean
    fun get(key: K): V?
    fun isEmpty(): Boolean
    fun register(key: K, value: V)
}