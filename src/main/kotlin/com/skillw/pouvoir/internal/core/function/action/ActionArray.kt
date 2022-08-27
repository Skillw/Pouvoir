package com.skillw.pouvoir.internal.core.function.action

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.IAction
import com.skillw.pouvoir.api.function.parser.Parser

/**
 * @className ActionArray
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object ActionArray : IAction {

    override val actions: Set<String>
        get() = hashSetOf("get", "set", "length")
    override val type: Class<*>
        get() = Array::class.java

    override fun action(parser: Parser, obj: Any, action: String): Any? {
        obj as? Array<Any>? ?: error("obj is not array")
        with(parser) {
            return when (action) {
                "get" -> {
                    except("at")
                    val index = parseInt()
                    obj[index]
                }

                "set" -> {
                    except("at")
                    val index = parseInt()
                    except("to")
                    val value = parseAny()
                    obj[index] = value
                    value
                }

                "length" -> obj.size
                else -> null
            }
        }
    }
}