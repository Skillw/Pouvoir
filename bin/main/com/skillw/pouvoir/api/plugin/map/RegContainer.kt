package com.skillw.pouvoir.api.plugin.map

/**
 * @className RegContainer
 *
 * @author Glom
 * @date 2022/7/30 20:35 Copyright 2022 user.
 *
 * 为了与java交互而写
 */
interface RegContainer<K, V> {
    val map: MutableMap<K, V>

    /** Size */
    val size: Int

    /** Entries */
    val entries: MutableSet<MutableMap.MutableEntry<K, V>>

    /** Keys */
    val keys: MutableSet<K>

    /** Values */
    val values: MutableCollection<V>

    /** Clear */
    fun clear()

    /**
     * Put
     *
     * @param key
     * @param value
     * @return
     */
    fun put(key: K, value: V): V?

    /**
     * Put all
     *
     * @param from
     */
    fun putAll(from: Map<out K, V>)

    /**
     * Remove
     *
     * @param key
     * @return
     */
    fun remove(key: K): V?

    /**
     * Contains key
     *
     * @param key
     * @return
     */
    fun containsKey(key: K): Boolean

    /**
     * Contains value
     *
     * @param value
     * @return
     */
    fun containsValue(value: V): Boolean

    /**
     * Get
     *
     * @param key
     * @return
     */
    fun get(key: K): V?

    /**
     * Is empty
     *
     * @return
     */
    fun isEmpty(): Boolean

    /**
     * Register
     *
     * @param key
     * @param value
     */
    fun register(key: K, value: V)
}