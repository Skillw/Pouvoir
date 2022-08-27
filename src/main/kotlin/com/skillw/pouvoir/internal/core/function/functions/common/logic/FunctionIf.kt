package com.skillw.pouvoir.internal.core.function.functions.common.logic


import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import taboolib.common.platform.function.warning
import java.util.function.Supplier

@AutoRegister
object FunctionIf : PouFunction<Any>("if") {

    override fun execute(parser: Parser): Any? {
        with(parser) {
            var bool = parseBoolean()
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
                return (if (peekNext() == "{") {
                    Supplier { parseBlock().execute(parser) }
                } else {
                    Supplier { parser.parseNext() }
                }).get().also {
                    except("else");
                    if (peekNext() == "{") {
                        parseBlock()
                    } else {
                        parseNext<Any?>()
                    }
                }
            }
            var ifFalse: Supplier<Any?>? = null
            if (skipTill("then", "else")) {
                ifFalse = if (peekNext() == "{") {
                    Supplier { parseBlock().execute(parser) }
                } else {
                    Supplier { parser.parseNext() }
                }
            }
            return ifFalse?.get()
        }
    }
}