package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.function.action.ActionExecutor
import com.skillw.pouvoir.api.function.action.PouAction
import com.skillw.pouvoir.api.function.action.PouAction.Companion.getAction
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.api.manager.sub.function.PouActionManager


/**
 * Inline function action manager
 *
 * @constructor Create empty Inline function manager
 */
object PouActionManagerImpl : PouActionManager() {
    override val key = "PouActionManager"
    override val priority = 4
    override val subPouvoir = Pouvoir
    override fun <T : Any> actionOf(any: T): PouAction<T>? {
        val clazz = any::class.java
        return (get(clazz)
            ?: (filterKeys { it.isAssignableFrom(clazz) }.values as Collection<PouAction<Any>>).merge<Any, T>(clazz)
                .also { it.register() }) as? PouAction<T>?
    }

    private fun <A : Any, T : A> Collection<PouAction<A>>.merge(clazz: Class<*>): PouAction<T> {
        return object : PouAction<T>(clazz as Class<T>) {
            private fun <A : Any> Parser.exec(exec: ActionExecutor<A>, obj: A): Any? {
                with(exec) {
                    return executor(obj)
                }
            }

            init {
                this@merge.forEach {
                    it.actions.forEach inner@{ action ->
                        val key = action.key
                        val exec = action.value
                        actions[key] = ActionExecutor { obj ->
                            val a = obj as? A? ?: return@ActionExecutor null
                            return@ActionExecutor exec(exec, a)
                        }
                    }
                }
            }
        }
    }

    private fun Parser.hasAction(obj: Any?): Boolean {
        return obj?.getAction()?.hasAction(this) ?: false
    }

    override fun action(parser: Parser, obj: Any): Any? {
        with(parser) {
            var value: Any? = obj
            while (value != null && hasAction(value)) {
                value = value.getAction()?.action(this, value, next().toString())
            }
            return value
        }
    }
}