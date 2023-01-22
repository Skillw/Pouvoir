package com.skillw.asahi.internal.namespacing.infix.linking

import com.skillw.asahi.api.annotation.AsahiInfix
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix

/**
 * @className ActionMap
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AsahiInfix
internal object InfixMap : BaseInfix<MutableMap<*, *>>(MutableMap::class.java) {

    init {
        infix("get") { map ->
            val key = parse<String>()
            map[key]
        }

        infix("put", "set") { map ->
            map as? MutableMap<String, Any?>? ?: error("MutableMap<String,Any?>")
            val key = parse<String>()
            expect("to")
            val value = parse<Any>()
            map[key] = value
            value
        }

        infix("putAll") { map ->
            map as? MutableMap<String, Any?>? ?: error("MutableMap<String,Any?>")
            val other = parse<Map<String, Any>>()
            map.putAll(other)
        }

        infix("remove") { map ->
            val key = parse<String>()
            map.remove(key)
        }

        infix("contains") { map ->
            val key = parse<String>()
            map.containsKey(key)
        }

        infix("size") { map ->
            map.size
        }

        infix("keys") { map ->
            map.keys
        }

        infix("values") { map ->
            map.values
        }

        infix("entries") { map ->
            map.entries
        }
    }

}