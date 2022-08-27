package com.skillw.pouvoir.internal.core.function.functions.common.math

import com.skillw.pouvoir.api.PouvoirAPI.analysis
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.util.CalculationUtils.calculateDouble

@AutoRegister
object FunctionCalculate : PouFunction<Double>("calculate") {
    override fun execute(parser: Parser): Double {
        val formula = parser.parseString().analysis(parser.context)
        return formula.calculateDouble()
    }

}