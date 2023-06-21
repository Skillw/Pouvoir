package com.skillw.asahi.internal.parser.prefix.top

import com.skillw.asahi.api.AsahiManager
import com.skillw.asahi.api.annotation.AsahiTopParser
import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.parser.prefix.TopPrefixParser
import com.skillw.asahi.api.member.quest.Quester

@AsahiTopParser
internal object NamespaceCallParser : TopPrefixParser<Any?>("namespace-call-function", 1) {
    override fun AsahiLexer.canParse(token: String): Boolean {
        if (!token.contains("::")) return false
        val splits = token.split("::")
        val namespace = AsahiManager.getNamespace(splits[0])
        return namespace.hasPrefix(splits[1])
    }

    override fun AsahiLexer.parse(token: String): Quester<Any?>? {
        val splits = token.split("::")
        val namespace = AsahiManager.getNamespace(splits[0])
        val function = namespace.getPrefix(splits[1]) ?: return null
        return function.parseWith(this@parse) as Quester<Any?>?
    }

    override fun <R> Quester<R>.topQuester(): Quester<R> {
        return object : Quester<R> {
            override fun AsahiContext.execute(): R {
                return this@topQuester.run(this)
            }

            override fun toString(): String {
                return "Namespace Call Quester - $key ${this@topQuester}"
            }
        }
    }

}