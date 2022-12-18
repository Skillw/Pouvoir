package com.skillw.pouvoir.internal.core.function.parser

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.api.function.parser.TokenParser

@AutoRegister
object ParserPair : TokenParser(Pair::class.java) {
    override fun parse(parser: Parser): Any {
        with(parser) {
            val first = parseAny()
            except("to")
            val second = parseAny()
            return first to second
        }
    }
}