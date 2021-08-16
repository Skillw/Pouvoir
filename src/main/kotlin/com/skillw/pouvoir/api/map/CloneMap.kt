package com.skillw.rpglib.api.map

import com.skillw.pouvoir.api.able.Cloneable
import com.skillw.pouvoir.api.map.KeyMap

/**
 * ClassName : com.skillw.com.skillw.rpglib.api.map.CloneMap
 * Created by Glom_ on 2021-04-10 22:47:39
 * Copyright  2021 user. All rights reserved.
 */
abstract class CloneMap<K, V : Cloneable<K, V>> : KeyMap<K, V>() {
    override fun get(k: K): V? {
        return map[k]?.clone()
    }
}