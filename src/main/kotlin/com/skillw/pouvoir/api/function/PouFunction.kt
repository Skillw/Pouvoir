package com.skillw.pouvoir.api.function

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.able.Registrable


/**
 * Pou function
 *
 * @constructor Create empty Pou function
 * @property key 函数键
 */
abstract class PouFunction<T>(
    override val key: String,
    vararg val aliases: String,
    val namespace: String = "common",
) : Registrable<String>, IFunction<T> {

    override fun register() {
        Pouvoir.pouFunctionManager.register(this)
        aliases.forEach {
            Pouvoir.pouFunctionManager.register(it, this)
        }
    }

    override fun toString(): String {
        return "PouFunction::$key"
    }

}