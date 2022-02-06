package com.skillw.pouvoir.api.map

open class LowerMap<V> : BaseMap<String, V>() {
    override fun get(key: String): V? {
        return super.get(key.lowercase())
    }

    override fun hasKey(key: String): Boolean {
        return super.hasKey(key.lowercase())
    }

    override fun put(key: String, value: V): V {
        return super.put(key.lowercase(), value)
    }

    override fun remove(key: String): V? {
        return super.remove(key.lowercase())
    }
}