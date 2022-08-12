package com.skillw.pouvoir.internal.function.functions.common.str

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser
import com.skillw.pouvoir.util.StringUtils.replacement


@AutoRegister
object FunctionReplace : PouFunction<String>(
    "replace"
) {
    override fun execute(parser: Parser): String? {
        with(parser) {
            val replacement = HashMap<String, String>()
            val str = parseString()
            if (!except("[")) return null
            while (hasNext()) {
                val origin = parseString()
                except("to")
                val replaced = parseString()
                replacement[origin] = replaced
                if (except(",")) continue
                else if (except("]")) break
            }
            return str.replacement(replacement)
        }
    }
}



