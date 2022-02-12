package com.skillw.pouvoir.api.map

import java.util.concurrent.CopyOnWriteArrayList

open class LinkedMap<K, V> : BaseMap<K, V>() {
    protected val list = CopyOnWriteArrayList<K>()

    override fun put(key: K, value: V): V {
        list.add(key)
        return super.put(key, value)
    }
}