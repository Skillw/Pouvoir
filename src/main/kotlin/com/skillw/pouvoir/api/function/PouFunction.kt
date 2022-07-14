package com.skillw.pouvoir.api.function

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.able.Keyable
import java.util.function.Function

abstract class PouFunction(
    override val key: String,
) : Keyable<String>,
    Function<Array<String>, Any?> {

    protected abstract fun predicate(args: Array<String>): Boolean
    protected abstract fun function(args: Array<String>): Any?

    override fun register() {
        Pouvoir.functionManager.register(this)
    }

    override fun apply(args: Array<String>) = if (predicate(args)) function(args) else null

}