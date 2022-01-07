package com.skillw.pouvoir.api.map

import com.skillw.pouvoir.api.able.Keyable

open class LowerKeyMap<V : Keyable<String>> : KeyMap<String, V>() {
    override operator fun get(key: String): V? {
        return super.get(key.lowercase())
    }

    override fun hasKey(key: String): Boolean {
        return super.hasKey(key.lowercase())
    }

    override fun removeByKey(key: String) {
        super.removeByKey(key.lowercase())
    }

    override fun put(key: String, value: V): V {
        return put(key.lowercase(), value)
    }
}