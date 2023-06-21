package com.skillw.asahi.api.member.parser.prefix.type

import com.skillw.asahi.api.AsahiManager
import com.skillw.asahi.api.member.AsahiRegistrable
import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.parser.prefix.PrefixParser
import com.skillw.asahi.api.member.quest.Quester

/**
 * @className TypeParser
 *
 * 类型解释器
 *
 * 根据上一个Quester获取的对象类型来选取解释器
 *
 * @author Glom
 * @date 2022/12/27 10:12 Copyright 2022 user. All rights reserved.
 */
abstract class TypeParser<R>(vararg types: Class<*>) : AsahiRegistrable<Set<Class<*>>>, PrefixParser<R> {
    override val key: Set<Class<*>> = setOf(*types)

    /**
     * 类型解释器内容
     *
     * @return 解释结果
     */
    protected abstract fun AsahiLexer.parse(): Quester<R>
    override fun parseWith(lexer: AsahiLexer): Quester<R> {
        lexer.previous()
        return lexer.parse().typeQuester()
    }

    private fun <R> Quester<R>.typeQuester(): Quester<R> {
        return object : Quester<R> {
            override fun AsahiContext.execute(): R {
                return this@typeQuester.run(this)
            }

            override fun toString(): String {
                return "Type Quester - $key"
            }
        }
    }

    override fun register() {
        AsahiManager.registerParser(this)
    }
}

