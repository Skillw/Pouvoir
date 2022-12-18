package com.skillw.pouvoir.internal.core.function.parser

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.IFunction
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.api.function.parser.TokenParser

@AutoRegister
object ParserFunction : TokenParser(IFunction::class.java) {

    override fun parse(parser: Parser): Any? {
        with(parser) {
            val function = context.functions[next().toString()] ?: return null
            return function.execute(this)
        }
    }
}