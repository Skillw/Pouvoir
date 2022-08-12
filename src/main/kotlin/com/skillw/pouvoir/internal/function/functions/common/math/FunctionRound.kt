package com.skillw.pouvoir.internal.function.functions.common.math

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser
import kotlin.math.round

@AutoRegister
object FunctionRound : PouFunction<Int>("round") {

    override fun execute(parser: Parser): Int {
        val x = parser.parseDouble()
        return round(x).toInt()
    }
}