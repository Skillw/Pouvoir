package com.skillw.pouvoir.internal.core.function.parser

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.api.function.parser.TokenParser

@AutoRegister
object ArrayParser : TokenParser(Array::class.java) {
    override fun parse(parser: Parser): Any {
        return (ListParser.parse(parser) as List<Any>).toTypedArray()
    }

}