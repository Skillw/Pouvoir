package com.skillw.pouvoir.api.function.action

import com.skillw.pouvoir.Pouvoir.pouActionManager
import com.skillw.pouvoir.api.able.Registrable
import com.skillw.pouvoir.api.function.parser.Parser

/**
 * @className PouAction
 *
 * @author Glom
 * @date 2022/8/14 16:43 Copyright 2022 user. All rights reserved.
 */
abstract class PouAction<T : Any>(override val key: Class<T>) : Registrable<Class<T>> {
    internal val actions = HashMap<String, ActionExecutor<T>>()

    constructor(type: Class<T>, vararg pairs: Pair<String, ActionExecutor<T>>) : this(type) {
        actions.putAll(pairs)
    }

    fun action(parser: Parser, obj: T, action: String): Any? {
        with(parser) {
            return actions[action]?.run {
                executor(obj)
            }
        }
    }

    fun hasAction(parser: Parser): Boolean {
        with(parser) {
            return actions.containsKey(peek())
        }
    }

    fun addExec(key: String, executor: Parser.(T) -> Any?): PouAction<T> {
        actions[key] = ActionExecutor { executor(it) }
        return this
    }

    fun removeExec(key: String): PouAction<T> {
        actions.remove(key)
        return this
    }

    override fun register() {
        pouActionManager.register(key, this)
    }

    companion object {
        @JvmStatic
        fun <T : Any> T.getAction(): PouAction<T>? {
            return pouActionManager.actionOf(this)
        }

        @JvmStatic
        fun <T : Any> createAction(clazz: Class<T>, receiver: PouAction<T>.() -> Unit): PouAction<T> {
            return object : PouAction<T>(clazz) {}.apply(receiver)
        }
    }
}