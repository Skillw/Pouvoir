package com.skillw.pouvoir.internal.core.function.functions.common.io

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser


@AutoRegister
object FunctionPrint : PouFunction<Any>(
    "print"
) {
    override fun execute(parser: Parser): Any? {
        with(parser) {
            val value = parseNext<Any?>()
            println(value)
            return value
        }
    }
}



