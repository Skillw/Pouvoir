package com.skillw.asahi.internal.namespacing.prefix.lang.math

import com.skillw.asahi.api.AsahiAPI.analysis
import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest

/**
 * @className Calculate
 *
 * @author Glom
 * @date 2023/1/14 0:31 Copyright 2023 user. All rights reserved.
 */
@AsahiPrefix(["calculate", "calc"], "lang")
private fun calculate() = prefixParser {
    val formulaGetter = quest<String>()
    result {
        val formula = formulaGetter.get().analysis(this, *namespaceNames())
        com.skillw.pouvoir.util.calculate(formula)
    }
}