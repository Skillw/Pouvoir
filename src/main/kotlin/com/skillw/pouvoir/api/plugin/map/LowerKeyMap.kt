package com.skillw.pouvoir.api.plugin.map

import com.skillw.pouvoir.api.plugin.map.component.Keyable


/**
 * Lower key map
 *
 * @param V
 * @constructor Create empty Lower key map
 */
open class LowerKeyMap<V : Keyable<String>> : KeyMap<String, V>() {

    override operator fun get(key: String): V? {
        return super.get(key.lowercase())
    }

    override fun remove(key: String): V? {
        return super.remove(key.lowercase())
    }

    override fun containsKey(key: String): Boolean {
        return super.containsKey(key.lowercase())
    }

    override operator fun set(key: String, value: V): V? {
        return super.set(key.lowercase(), value)
    }

    override fun put(key: String, value: V): V? {
        return super.put(key.lowercase(), value)
    }
}