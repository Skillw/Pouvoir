package com.skillw.pouvoir.internal.core.function.parser

import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.api.function.parser.TokenParser
import com.skillw.pouvoir.internal.core.function.functions.common.define.FunctionSet
import java.util.function.Supplier

object VarParser : TokenParser(Nothing::class.java) {

    override fun parse(parser: Parser): Any? {
        with(parser) {
            val token = next().toString()
            val variable = context[token.replace(
                "&",
                ""
            )].let action@{
                when (it) {
                    is FunctionSet.ILazy -> {
                        it.execute(this) ?: return null
                    }

                    is Supplier<*> -> {
                        it.get()
                    }

                    else -> it
                }
            }
            return variable
        }
        return null
    }
}