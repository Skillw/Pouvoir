package com.skillw.pouvoir.internal.core.function.action

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.IAction
import com.skillw.pouvoir.api.function.parser.Parser

/**
 * @className ActionList
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object ActionList : IAction {

    override val actions: Set<String>
        get() = hashSetOf(
            "get",
            "add",
            "remove",
            "clear",
            "set",
            "size",
            "contains",
            "isEmpty",
            "toArray",
            "toString",
            "merge"
        )
    override val type: Class<*>
        get() = MutableList::class.java

    override fun action(parser: Parser, obj: Any, action: String): Any {
        obj as? MutableList<Any>? ?: error("obj is not a MutableList")
        with(parser) {
            return when (action) {
                "get" -> {
                    except("at")
                    val index = parseInt()
                    obj[index]
                }

                "add" -> {
                    val value = parseAny()
                    obj.add(value)
                }

                "remove" -> {
                    if (except("at")) {
                        return@with obj.remove(parseInt())
                    }
                    val value = parseAny()
                    obj.remove(value)
                }

                "clear" -> {
                    obj.clear()
                }

                "set" -> {
                    val index = parseInt()
                    val value = parseAny()
                    obj[index] = value
                }

                "size" -> {
                    obj.size
                }

                "contains" -> {
                    val value = parseAny()
                    obj.contains(value)
                }

                "isEmpty" -> {
                    obj.isEmpty()
                }

                "toArray" -> {
                    obj.toTypedArray()
                }

                "toString" -> {
                    obj.toString()
                }

                "merge" -> {
                    except("by")
                    val by = parseString()
                    obj.joinToString { by.replace("\\n", "\n") }
                }


                else -> error("action $action not found")

            }
        }
        return Unit
    }
}