package com.skillw.pouvoir.api.map

import com.skillw.pouvoir.api.able.Cloneable

/**
 * ClassName : com.skillw.com.skillw.rpglib.api.map.CloneMap
 * Created by Glom_ on 2021-04-10 22:47:39
 * Copyright  2021 user. All rights reserved.
 */
open class CloneMap<K, V : Cloneable<K, V>> : KeyMap<K, V>() {
    override fun get(key: K): V? {
        return map[key]?.clone()
    }
}