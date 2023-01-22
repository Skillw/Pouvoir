package com.skillw.asahi.internal.parser.prefix.top

import com.skillw.asahi.api.annotation.AsahiTopParser
import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.parser.prefix.TopPrefixParser
import com.skillw.asahi.api.member.quest.Quester

@AsahiTopParser
internal object PrefixCallParser : TopPrefixParser<Any?>("call-function", 999) {
    override fun AsahiLexer.canParse(token: String): Boolean {
        return hasPrefix(token)
    }

    override fun AsahiLexer.parse(token: String): Quester<Any?>? {
        return getPrefix(token).firstOrNull()?.parseWith(this@parse) as Quester<Any?>?
    }

    override fun <R> Quester<R>.topQuester(): Quester<R> {
        return object : Quester<R> {
            override fun AsahiContext.execute(): R {
                return this@topQuester.run(this)
            }

            override fun toString(): String {
                return "Prefix Quester - $key ${this@topQuester}"
            }
        }
    }

}