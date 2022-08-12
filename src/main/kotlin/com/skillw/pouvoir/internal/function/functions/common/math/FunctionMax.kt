package com.skillw.pouvoir.internal.function.functions.common.math

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser
import kotlin.math.max

@AutoRegister
object FunctionMax : PouFunction<Double>("max") {

    override fun execute(parser: Parser): Double {
        with(parser) {
            val x = parseDouble()
            val y = parseDouble()
            return max(x, y)
        }
    }
}