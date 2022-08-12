package com.skillw.pouvoir.internal.function.functions.common.math

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser
import kotlin.math.abs

@AutoRegister
object FunctionAbs : PouFunction<Double>("abs") {
    override fun execute(parser: Parser): Double {
        val number = parser.parseDouble()
        return abs(number)
    }
}