package com.skillw.pouvoir.internal.core.function.functions.common.str

import com.skillw.pouvoir.api.PouvoirAPI.analysis
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser


@AutoRegister
object FunctionAnalysis : PouFunction<String>(
    "analysis"
) {
    override fun execute(parser: Parser): String {
        with(parser) {
            val text = parseString()
            return text.analysis(context)
        }
    }
}



