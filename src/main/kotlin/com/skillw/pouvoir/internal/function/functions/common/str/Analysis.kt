package com.skillw.pouvoir.internal.function.functions.common.str

import com.skillw.pouvoir.api.PouvoirAPI.analysis
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser


@AutoRegister
object Analysis : PouFunction<String>(
    "analysis"
) {
    override fun execute(parser: Parser): String? {
        with(parser) {
            val text = parseString(context) ?: return null
            return text.analysis(context)
        }
    }
}



