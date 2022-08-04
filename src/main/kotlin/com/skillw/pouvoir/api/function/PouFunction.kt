package com.skillw.pouvoir.api.function

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.able.Registrable
import com.skillw.pouvoir.api.function.context.Context
import com.skillw.pouvoir.api.function.reader.IReader

/**
 * Pou function
 *
 * @constructor Create empty Pou function
 * @property key 函数键
 */
abstract class PouFunction<T>(
    override val key: String,
    val namespace: String = "common",
) : Registrable<String> {
    abstract fun execute(reader: IReader, context: Context): T?

    override fun register() {
        Pouvoir.pouFunctionManager.register(this)
    }


}