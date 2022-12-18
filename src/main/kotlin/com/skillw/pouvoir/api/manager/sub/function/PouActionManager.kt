package com.skillw.pouvoir.api.manager.sub.function

import com.skillw.pouvoir.api.function.action.PouAction
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.BaseMap


/**
 * Inline function action manager
 *
 * @constructor Create empty Inline function manager
 */
abstract class PouActionManager : BaseMap<Class<*>, PouAction<*>>(), Manager {
    abstract fun <T : Any> actionOf(any: T): PouAction<T>?
    abstract fun action(parser: Parser, obj: Any): Any?
}