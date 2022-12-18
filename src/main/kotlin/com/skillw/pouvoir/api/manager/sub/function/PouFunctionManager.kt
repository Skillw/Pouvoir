package com.skillw.pouvoir.api.manager.sub.function

import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.IContext
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.internal.core.function.context.SimpleContext


/**
 * Inline function manager
 *
 * @constructor Create empty Inline function manager
 */
abstract class PouFunctionManager : KeyMap<String, PouFunction<*>>(), Manager {

    /** Namespaces */
    abstract val namespaces: BaseMap<String, HashSet<PouFunction<*>>>

    abstract fun eval(
        string: String,
        namespaces: Array<String> = arrayOf("common"),
        context: IContext = SimpleContext(),
    ): Any?

    abstract fun eval(
        string: String,
        namespaces: Array<String> = arrayOf("common"),
        receiver: IContext.() -> Unit = {},
    ): Any?

}