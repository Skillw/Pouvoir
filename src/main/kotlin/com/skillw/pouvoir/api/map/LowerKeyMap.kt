package com.skillw.pouvoir.api.map

import com.skillw.pouvoir.api.able.Keyable


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

    override fun put(key: String, value: V): V {
        return super.put(key.lowercase(), value)
    }
}