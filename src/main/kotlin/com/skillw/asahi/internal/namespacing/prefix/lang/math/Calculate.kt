package com.skillw.asahi.internal.namespacing.prefix.lang.math

import com.skillw.asahi.api.AsahiAPI.analysis
import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import com.skillw.pouvoir.util.calculate.CalcOperator.Companion.toCalcOperator

/**
 * @className Calculate
 *
 * @author Glom
 * @date 2023/1/14 0:31 Copyright 2023 user. All rights reserved.
 */
@AsahiPrefix(["calculate", "calc"], "lang")
private fun calculate() = prefixParser<Double> {
    val formulaGetter = quest<String>()
    result {
        val formula = formulaGetter.get().analysis(this, *namespaceNames())
        com.skillw.pouvoir.util.calculate(formula)
    }
}

@AsahiPrefix(["math"], "lang")
private fun math() = prefixParser<Double> {
    val numA = questDouble()
    val operator = questString().quester { it.first() }
    val numB = questDouble()
    result {
        operator.get().toCalcOperator().calc(numB.get(), numA.get())
    }
}