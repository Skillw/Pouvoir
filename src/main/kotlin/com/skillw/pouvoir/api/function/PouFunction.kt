package com.skillw.pouvoir.api.function

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.able.Keyable
import java.util.function.Function
import java.util.function.Predicate

open class PouFunction(
    override val key: String,
    private val predicate: Predicate<Array<String>>,
    private val func: Function<Array<String>, Any?>
) : Keyable<String>,
    Function<Array<String>, Any?> {

    override fun register() {
        Pouvoir.functionManager.register(this)
    }

    override fun apply(args: Array<String>): Any? {
        if (!predicate.test(args)) {
            return null
        }
        return func.apply(args)
    }

}