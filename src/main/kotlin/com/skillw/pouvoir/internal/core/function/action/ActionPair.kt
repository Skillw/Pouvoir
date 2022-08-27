package com.skillw.pouvoir.internal.core.function.action

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.IAction
import com.skillw.pouvoir.api.function.parser.Parser

/**
 * @className ActionMap
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object ActionPair : IAction {
    override val actions: Set<String> =
        hashSetOf("key", "value")
    override val type: Class<*> = Pair::class.java

    override fun action(parser: Parser, obj: Any, action: String): Any? {
        obj as? Pair<Any, Any>? ?: error("$obj is not a Pair")
        with(parser) {
            when (action) {
                "key" -> return obj.first
                "value" -> return obj.second
                else -> {
                    error("unknown pair $obj action $action")
                }
            }
        }
    }

}