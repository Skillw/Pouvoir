package com.skillw.pouvoir.api.plugin.map

/**
 * @className RegContainer
 *
 * @author Glom
 * @date 2022/7/30 20:35 Copyright 2022 user. All rights reserved.
 *
 * 为了与java交互而写
 */
interface RegContainer<K, V> : MutableMap<K, V> {


    /**
     * Register
     *
     * @param key
     * @param value
     */
    fun register(key: K, value: V): V?

    // Modification Operations
    /**
     * Associates the specified [value] with the specified [key] in the map.
     *
     * @return the previous value associated with the key, or `null` if the key
     *     was not present in the map.
     */
    override fun put(key: K, value: V): V?

    /**
     * Removes the specified key and its corresponding value from this map.
     *
     * @return the previous value associated with the key, or `null` if the key
     *     was not present in the map.
     */
    override fun remove(key: K): V?

    // Bulk Modification Operations
    /** Updates this map with key/value pairs from the specified map [from]. */
    override fun putAll(from: Map<out K, V>): Unit

    /** Removes all elements from this map. */
    override fun clear(): Unit

    // Views
    /** Returns a [MutableSet] of all keys in this map. */
    override val keys: MutableSet<K>

    /**
     * Returns a [MutableCollection] of all values in this map. Note that this
     * collection may contain duplicate values.
     */
    override val values: MutableCollection<V>

    /** Returns a [MutableSet] of all key/value pairs in this map. */
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
}