package com.skillw.pouvoir.api.plugin.map

import com.skillw.pouvoir.api.plugin.map.component.Keyable
import java.util.function.BiFunction
import java.util.function.Function

/**
 * ClassName : com.skillw.classsystem.api.plugin.map.KeyMap Created by
 * Glom_ on 2021-03-26 22:03:17 Copyright 2021 user. All rights reserved.
 */
open class KeyMap<K : Any, V : Keyable<K>> : BaseMap<K, V>() {
    private fun getKey(value: V): K {
        return value.key
    }

    override operator fun get(key: K): V? {
        return super.get(key)
    }

    override fun remove(key: K): V? {
        return super.remove(key)
    }

    override fun containsKey(key: K): Boolean {
        return super.containsKey(key)
    }

    open operator fun set(key: K, value: V): V? {
        return super.put(key, value)
    }

    override fun put(key: K, value: V): V? {
        return super.put(key, value)
    }

    override fun putAll(from: Map<out K, V>) {
        super.putAll(from.mapKeys { it.key })
    }

    override fun computeIfAbsent(key: K, mappingFunction: Function<in K, out V>): V {
        return super.computeIfAbsent(key, mappingFunction)
    }

    override fun compute(key: K, remappingFunction: BiFunction<in K, in V?, out V?>): V? {
        return super.compute(key, remappingFunction)
    }

    override fun merge(key: K, value: V, remappingFunction: BiFunction<in V, in V, out V?>): V? {
        return super.merge(key, value, remappingFunction)
    }

    override fun putIfAbsent(key: K, value: V): V? {
        return super.putIfAbsent(key, value)
    }

    /**
     * Register
     *
     * @param value
     */
    open fun register(value: V) {
        register(value.key, value)
    }

    /**
     * Remove by value
     *
     * @param value
     */
    fun removeByValue(value: V) {
        val key = getKey(value)
        key.also { remove(key) }
    }
}