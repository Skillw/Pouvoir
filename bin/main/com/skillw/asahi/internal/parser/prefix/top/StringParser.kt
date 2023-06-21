package com.skillw.asahi.internal.parser.prefix.top

import com.skillw.asahi.api.annotation.AsahiTopParser
import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.parser.prefix.TopPrefixParser
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.quester


@AsahiTopParser
internal object StringParser : TopPrefixParser<String>("string", 999) {
    private val symbols = arrayOf('\'', '"')
    override fun AsahiLexer.canParse(token: String): Boolean {
        return isWrapping(token)
    }

    fun isWrapping(str: String): Boolean {
        return str.first() in symbols && str.last() in symbols
    }

    fun content(str: String): String {
        return if (isWrapping(str)) {
            str.substring(1, str.lastIndex)
        } else str
    }

    override fun AsahiLexer.parse(token: String): Quester<String> {
        return quester {
            content(token)
        }
    }
}