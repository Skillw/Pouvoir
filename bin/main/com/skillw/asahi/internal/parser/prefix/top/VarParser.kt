package com.skillw.asahi.internal.parser.prefix.top

import com.skillw.asahi.api.annotation.AsahiTopParser
import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.parser.prefix.TopPrefixParser
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.quester
import java.util.function.Supplier

@AsahiTopParser
internal object VarParser : TopPrefixParser<Any?>("var", 1) {
    private val varPrefixes = arrayOf('&', '$')
    override fun AsahiLexer.canParse(token: String): Boolean {
        return isCallVar(token)
    }

    fun isCallVar(token: String): Boolean {
        return token.first() in varPrefixes
    }

    override fun AsahiLexer.parse(token: String): Quester<Any?> {
        val varKey = token.substring(1)
        return quester {
            get(varKey)?.let {
                when (it) {
                    is Supplier<*> -> it.get()
                    else -> it
                }
            } ?: token
        }
    }
}