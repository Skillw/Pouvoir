package com.skillw.pouvoir.internal.core.function.action

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.action.IAction
import com.skillw.pouvoir.api.function.parser.Parser

/**
 * @className ActionMap
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object ActionMap : PouFunction<Any>(
    "map"
), IAction {
    override val actions: Set<String> =
        hashSetOf("get", "set", "put", "remove", "clear", "contains", "size", "keys", "values", "entries")
    override val type: Class<*> = MutableMap::class.java

    override fun execute(parser: Parser): Any? {
        with(parser) {
            return action(parser, parseMap(), parseString())
        }
    }

    override fun action(parser: Parser, obj: Any, action: String): Any? {
        obj as? MutableMap<String, Any>? ?: error("$obj is not a mutable map")
        with(parser) {
            when (action) {
                "get" -> {
                    val key = parseString()
                    return obj[key]
                }

                "set" -> {
                    val key = parseString()
                    val value = parseAny()
                    obj[key] = value
                    return value
                }

                "put" -> {
                    val key = parseString()
                    val value = parseAny()
                    obj[key] = value
                    return value
                }

                "remove" -> {
                    val key = parseString()
                    return obj.remove(key)
                }

                "contains" -> {
                    val key = parseString()
                    return obj.containsKey(key)
                }

                "size" -> {
                    return obj.size
                }

                "keys" -> {
                    return obj.keys
                }

                "values" -> {
                    return obj.values
                }

                "entries" -> {
                    return obj.entries
                }

                else -> {
                    error("unknown map action $action")
                }
            }
        }
    }

}