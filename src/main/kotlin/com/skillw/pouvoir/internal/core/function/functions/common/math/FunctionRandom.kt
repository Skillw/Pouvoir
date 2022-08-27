package com.skillw.pouvoir.internal.core.function.functions.common.math

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import taboolib.common.util.random

@AutoRegister
object FunctionRandom : PouFunction<Double>("random") {
    override fun execute(parser: Parser): Double {
        with(parser) {
            val x = parseDouble()
            except("to")
            val y = parseDouble()
            return random(x, y)
        }
    }
}