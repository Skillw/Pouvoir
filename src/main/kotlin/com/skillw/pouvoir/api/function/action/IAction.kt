package com.skillw.pouvoir.api.function.action

import com.skillw.pouvoir.api.function.parser.Parser

/**
 * @className IAction
 *
 * @author Glom
 * @date 2022/8/14 16:43 Copyright 2022 user. All rights reserved.
 */
interface IAction {

    val actions: Set<String>
    val type: Class<*>
    fun action(parser: Parser, obj: Any, action: String): Any?

    companion object {
        private val actions = HashMap<Class<*>, IAction>()

        @JvmStatic
        fun register(action: IAction) {
            actions[action.type] = action
        }

        fun Any.getAction(): IAction? {
            return actions[this::class.java]
                ?: actions.values.firstOrNull { it.type.isAssignableFrom(this::class.java) }
        }

        fun Parser.action(result: Any?): Any? {
            val action = result?.getAction() ?: return result
            val next = peekNext() ?: return result
            return if (next in action.actions) action.action(this, result, next().toString()) else result
        }
    }
}