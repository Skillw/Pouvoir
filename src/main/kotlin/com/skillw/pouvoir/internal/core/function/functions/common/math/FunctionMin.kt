package com.skillw.pouvoir.internal.core.function.functions.common.math

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import kotlin.math.min

@AutoRegister
object FunctionMin : PouFunction<Double>("min") {
    override fun execute(parser: Parser): Double {
        with(parser) {
            val x = parseDouble()
            val y = parseDouble()
            return min(x, y)

        }
    }
}