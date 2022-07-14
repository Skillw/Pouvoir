package com.skillw.pouvoir.api.map

import com.skillw.pouvoir.api.able.Keyable

open class LinkedKeyMap<K, V : Keyable<K>> : LinkedMap<K, V>() {
    private fun getKey(value: V): K {
        return value.key
    }

    open fun register(value: V) {
        register(value.key, value)
    }

    fun removeByValue(value: V) {
        val key = getKey(value)
        key?.also { map.remove(it) }
    }
}