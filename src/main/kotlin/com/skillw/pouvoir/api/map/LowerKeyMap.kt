package com.skillw.rpglib.api.map

import com.skillw.pouvoir.api.able.Keyable
import com.skillw.pouvoir.api.map.KeyMap

abstract class LowerKeyMap<V : Keyable<String>> : KeyMap<String, V>() {
    override operator fun get(k: String): V? {
        return super.get(k.lowercase())
    }

    override fun hasKey(k: String): Boolean {
        return super.hasKey(k.lowercase())
    }

    override fun removeByKey(k: String) {
        super.removeByKey(k.lowercase())
    }

    override fun register(k: String, v: V) {
        super.register(k.lowercase(), v)
    }
}