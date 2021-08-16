package com.skillw.pouvoir.api.map

import com.skillw.pouvoir.api.able.Keyable
import com.skillw.rpglib.api.map.BaseMap

/**
 * ClassName : com.skillw.classsystem.api.map.KeyMap
 * Created by Glom_ on 2021-03-26 22:03:17
 * Copyright  2021 user. All rights reserved.
 */
abstract class KeyMap<K, V : Keyable<K>> : BaseMap<K, V>() {
    protected fun getKey(v: V): K {
        return v.key
    }

    open fun register(v: V) {
        map[getKey(v)] = v
    }

    fun removeByValue(v: V) {
        map.remove(getKey(v))
    }
}