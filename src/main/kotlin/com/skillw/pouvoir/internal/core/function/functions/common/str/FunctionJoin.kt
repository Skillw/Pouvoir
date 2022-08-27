package com.skillw.pouvoir.internal.core.function.functions.common.str

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser


@AutoRegister
object FunctionJoin : PouFunction<String>(
    "join"
) {
    override fun execute(parser: Parser): String {
        with(parser) {
            val list = parseList()
            val by = if (!except("by")) "" else parseString()
            return list.joinToString(by.replace("\\n", "\n"))
        }
    }
}



