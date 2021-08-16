package com.skillw.rpglib.api.map

abstract class LowerMap<V> : BaseMap<String, V?>() {
    override fun get(k: String): V? {
        return super.get(k.lowercase())
    }

    override fun hasKey(k: String): Boolean {
        return super.hasKey(k.lowercase())
    }

    override fun removeByKey(k: String) {
        super.removeByKey(k.lowercase())
    }

    override fun register(k: String, v: V?) {
        super.register(k.lowercase(), v)
    }
}