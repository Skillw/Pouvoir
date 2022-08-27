package com.skillw.pouvoir.internal.core.function.parser

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.api.function.parser.TokenParser

@AutoRegister
object StringParser : TokenParser(String::class.java) {

    override fun parse(parser: Parser): Any {
        with(parser) {
            val token = next().toString()
            if (token == "null") return ""
            if (token == "pass") return ""
            if (token.first() == '\'') {
                if (token.last() == '\'' && token.length != 1) return token.replace("\'", "")
                val builder = StringBuilder(token.replace("\'", ""))
                while (hasNext()) {
                    if (next()?.last() != '\'')
                        builder.append(" " + current())
                    else {
                        builder.append(" " + current().replace("\'", ""))
                        break
                    }
                }
                return builder.toString().replace("\\_", " ")
            }
            return token
        }
    }
}