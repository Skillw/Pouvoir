package com.skillw.pouvoir.internal.core.function.functions.common.str

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.util.StringUtils.replacement


@AutoRegister
object FunctionReplace : PouFunction<String>(
    "replace"
) {
    override fun execute(parser: Parser): String? {
        with(parser) {
            val str = parseString()
            val replacement = parseMap()
            return str.replacement(replacement.mapValues { it.value.toString() })
        }
    }
}



