package com.skillw.pouvoir.internal.function.functions.common.math

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser
import kotlin.math.floor

@AutoRegister
object FunctionFloor : PouFunction<Double>("floor") {

    override fun execute(parser: Parser): Double {
        val number = parser.parseDouble()
        return floor(number)
    }
}