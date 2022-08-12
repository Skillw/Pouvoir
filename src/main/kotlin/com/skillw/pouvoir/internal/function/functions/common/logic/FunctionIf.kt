package com.skillw.pouvoir.internal.function.functions.common.logic


import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser
import taboolib.common.platform.function.warning
import taboolib.common5.Coerce

@AutoRegister
object FunctionIf : PouFunction<Any>("if") {

    private fun Any.toBool() = Coerce.toBoolean(this)

    override fun execute(parser: Parser): Any? {
        with(parser) {
            var bool = parseBoolean(context) ?: return null
            val next = next() ?: return bool
            when (next) {
                "||" -> if (!bool) bool =
                    parseBoolean().also { if (it) except("then") } else skipTill("if", "then")

                "&&" -> if (bool) bool =
                    parseBoolean().also { if (it) except("then") }

                "then" -> {}

                else -> {
                    warning("Unknown token $next!")
                    return null
                }
            }
            if (bool) {
                val result = parseAny() ?: return null
                next()
                parseAny()
                return result
            } else {
                if (!skipTill("if", "else")) return null
                return parseAny()
            }
        }
    }
}