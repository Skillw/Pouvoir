package com.skillw.asahi.api.member.parser.prefix.namespacing

import com.skillw.asahi.api.AsahiManager
import com.skillw.asahi.api.member.AsahiRegistrable
import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.namespace.Namespacing
import com.skillw.asahi.api.member.parser.prefix.PrefixParser
import com.skillw.asahi.api.member.quest.Quester


/**
 * @className BasePrefix
 *
 * 前缀解释器
 *
 * 当词法器读到的token在names中时，则选择此前缀解释器进行解释
 *
 * @author Glom
 * @date 2022/12/25 13:39 Copyright 2022 user. All rights reserved.
 */
abstract class BasePrefix<R>(
    override val key: String,
    vararg val alias: String,
    override val namespace: String = "common",
) : PrefixParser<R>, AsahiRegistrable<String>, Namespacing {
    /** Tokens */
    val names: Set<String>
        get() = setOf(key, *alias)

    /**
     * 解释器执行内容
     *
     * @return 结果
     */
    protected abstract fun AsahiLexer.parse(): Quester<R>


    override fun parseWith(lexer: AsahiLexer): Quester<R> {
        val parser = lexer.parse()
        return object : Quester<R> {
            override fun AsahiContext.execute(): R {
                return parser.get()
            }

            override fun toString(): String {
                return "$key in namespace $namespace"
            }
        }
    }

    override fun toString(): String {
        return "AsahiPrefix { $key ${alias.toList()} } in namespace $namespace"
    }

    override fun register() {
        AsahiManager.getNamespace(namespace).registerPrefix(this)
    }
}