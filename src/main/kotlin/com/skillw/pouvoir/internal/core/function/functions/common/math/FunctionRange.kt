package com.skillw.pouvoir.internal.core.function.functions.common.math

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser

@AutoRegister
object FunctionRange : PouFunction<ClosedRange<Double>>("range") {
    override fun execute(parser: Parser): ClosedRange<Double> {
        with(parser) {
            val from = parseDouble()
            except("to", "~", "..")
            val to = parseDouble()
            return from..to
        }
    }
}