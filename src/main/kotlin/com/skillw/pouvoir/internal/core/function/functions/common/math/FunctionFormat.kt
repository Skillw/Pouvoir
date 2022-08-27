package com.skillw.pouvoir.internal.core.function.functions.common.math

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.util.NumberUtils.format

@AutoRegister
object FunctionFormat : PouFunction<String>("format") {

    override fun execute(parser: Parser): String {
        val number = parser.parseDouble()
        val format = parser.parseString()
        return number.format(format)
    }
}