package com.skillw.pouvoir.api.plugin.map

import com.skillw.pouvoir.api.plugin.map.component.Keyable

/**
 * ClassName : com.skillw.classsystem.api.plugin.map.KeyMap Created by
 * Glom_ on 2021-03-26 22:03:17 Copyright 2021 user.
 */
open class KeyMap<K : Any, V : Keyable<K>> : BaseMap<K, V>() {
    private fun getKey(value: V): K {
        return value.key
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