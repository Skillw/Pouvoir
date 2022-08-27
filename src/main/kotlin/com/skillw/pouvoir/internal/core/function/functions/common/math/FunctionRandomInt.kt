package com.skillw.pouvoir.internal.core.function.functions.common.math

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.util.NumberUtils

@AutoRegister
object FunctionRandomInt : PouFunction<Int>("randomInt") {

    override fun execute(parser: Parser): Int {
        with(parser) {
            val x = parseInt()
            except("to")
            val y = parseInt()
            return NumberUtils.randomInt(x, y)
        }
    }
}