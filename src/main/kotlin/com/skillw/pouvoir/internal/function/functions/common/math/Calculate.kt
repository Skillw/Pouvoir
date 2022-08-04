package com.skillw.pouvoir.internal.function.functions.common.math

import com.skillw.pouvoir.api.PouvoirAPI.analysis
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.Context
import com.skillw.pouvoir.api.function.reader.IReader
import com.skillw.pouvoir.util.CalculationUtils.calculateDouble

@AutoRegister
object Calculate : PouFunction<Double>("calculate") {
    override fun execute(reader: IReader, context: Context): Double? {
        val formula = reader.parseString(context)?.analysis()?.filter { it != ' ' } ?: return null
        return formula.calculateDouble()
    }

}