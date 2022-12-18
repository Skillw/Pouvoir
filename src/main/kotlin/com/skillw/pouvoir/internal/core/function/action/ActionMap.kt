package com.skillw.pouvoir.internal.core.function.action

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.PouAction

/**
 * @className ActionMap
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object ActionMap : PouAction<MutableMap<*, *>>(MutableMap::class.java) {

    init {
        addExec("get") { map ->
            val key = parseString()
            map[key]
        }

        addExec("set") { map ->
            map as? MutableMap<String, Any?>? ?: error("MutableMap<String,Any?>")
            val key = parseString()
            val value = parseAny()
            map[key] = value
            value
        }

        addExec("put") { map ->
            map as? MutableMap<String, Any?>? ?: error("MutableMap<String,Any?>")
            val key = parseString()
            val value = parseAny()
            map[key] = value
            value
        }

        addExec("remove") { map ->
            val key = parseString()
            map.remove(key)
        }

        addExec("contains") { map ->
            val key = parseString()
            map.containsKey(key)
        }

        addExec("size") { map ->
            map.size
        }

        addExec("keys") { map ->
            map.keys
        }

        addExec("values") { map ->
            map.values
        }

        addExec("entries") { map ->
            map.entries
        }
    }

}