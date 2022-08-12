package com.skillw.pouvoir.internal.function.functions.common.define

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser

@AutoRegister
object FunctionHas : PouFunction<Boolean>("has") {
    override fun execute(parser: Parser): Boolean {
        with(parser) {
            val key = parseString()
            return containsKey(key)
        }
    }
}