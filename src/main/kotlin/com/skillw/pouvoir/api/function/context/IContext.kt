package com.skillw.pouvoir.api.function.context

import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.internal.core.function.context.SimpleContext

/**
 * SimpleContext
 *
 * @constructor Create empty SimpleContext
 */
interface IContext : MutableMap<String, Any> {
    val functions: MutableMap<String, PouFunction<*>>
    fun putAllContext(context: IContext) {
        putAll(context)
        functions.putAll(context.functions)
    }

    fun clone(): IContext {
        return SimpleContext().apply { putAllContext(this@IContext) }
    }

}