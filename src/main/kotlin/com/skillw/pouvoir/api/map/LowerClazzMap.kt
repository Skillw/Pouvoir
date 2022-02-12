package com.skillw.pouvoir.api.map

import com.skillw.pouvoir.api.able.Keyable

open class LowerClazzMap<T : Keyable<String>> : ClazzMap<T>() {
    override fun put(key: String, value: Class<out T>): Class<out T> {
        return super.put(key.lowercase(), value)
    }

    override fun get(key: String): Class<out T>? {
        return super.get(key.lowercase())
    }

    override fun remove(key: String): Class<out T>? {
        return super.remove(key.lowercase())
    }

    override fun containsKey(key: String): Boolean {
        return super.containsKey(key.lowercase())
    }
}