package com.skillw.pouvoir.internal.core.function.functions.common.define

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser

@AutoRegister
object FunctionHas : PouFunction<Boolean>("has") {
    override fun execute(parser: Parser): Boolean {
        with(parser) {
            val key = parseString()
            return containsKey(key)
        }
    }
}