package com.skillw.pouvoir.internal.core.function.functions.common.math

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import taboolib.common5.Coerce

@AutoRegister
object FunctionNumber : PouFunction<Double>("number") {
    override fun execute(parser: Parser): Double {
        return Coerce.toDouble(parser.parseString())
    }
}