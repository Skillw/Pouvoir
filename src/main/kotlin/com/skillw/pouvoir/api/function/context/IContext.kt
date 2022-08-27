package com.skillw.pouvoir.api.function.context

import com.skillw.pouvoir.api.function.PouFunction

/**
 * SimpleContext
 *
 * @constructor Create empty SimpleContext
 */
interface IContext : MutableMap<String, Any> {
    val functions: MutableMap<String, PouFunction<*>>
}