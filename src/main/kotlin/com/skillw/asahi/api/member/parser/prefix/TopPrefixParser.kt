package com.skillw.asahi.api.member.parser.prefix

import com.skillw.asahi.api.AsahiManager.topPrefixParsers
import com.skillw.asahi.api.member.AsahiRegistrable
import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.quest.Quester

/**
 * @className TopPrefixParser
 *
 * 顶级前缀解释器
 *
 * 主要负责: 解释变量 解释字符串 选取前缀解释器 选取中缀解释器 解释变量Bean动作
 *
 * @author Glom
 * @date 2023/1/12 23:50 Copyright 2023 user. All rights reserved.
 */
abstract class TopPrefixParser<R>(override val key: String, val priority: Int) : AsahiRegistrable<String>,
    PrefixParser<R>,
    Comparable<TopPrefixParser<*>> {
    /**
     * 能否解释
     *
     * @param token token
     * @return 能否解释
     */
    protected abstract fun AsahiLexer.canParse(token: String): Boolean

    /**
     * 解释
     *
     * @param token token
     * @return 结果
     */
    protected abstract fun AsahiLexer.parse(token: String): Quester<R>?

    /**
     * 能否解释
     *
     * @param lexer 词法器
     * @param token token
     * @return 能否解释
     * @return
     */
    fun canParseWith(lexer: AsahiLexer, token: String): Boolean {
        return lexer.canParse(token)
    }

    /**
     * 解释
     *
     * @param lexer 词法器
     * @param token token
     * @return 结果
     */
    fun parseWith(lexer: AsahiLexer, token: String): Quester<R>? {
        return lexer.parse(token)?.topQuester()
    }

    override fun parseWith(lexer: AsahiLexer): Quester<R>? {
        return parseWith(lexer, lexer.next()) ?: kotlin.run { lexer.previous();null }
    }

    /**
     * 转为 Top Quester
     *
     * @param R
     * @return
     */
    protected open fun <R> Quester<R>.topQuester(): Quester<R> {
        return object : Quester<R> {
            override fun AsahiContext.execute(): R {
                return this@topQuester.run(this)
            }

            override fun toString(): String {
                return "Top Quester - $key ($priority)"
            }
        }
    }

    override fun compareTo(other: TopPrefixParser<*>): Int = if (this.priority == other.priority) 0
    else if (this.priority > other.priority) 1
    else -1

    override fun toString(): String {
        return "${javaClass.simpleName} { $key ($priority) }"
    }

    override fun register() {
        topPrefixParsers.add(this)
        topPrefixParsers.sorted()
    }

    companion object {
        @JvmStatic
        fun AsahiLexer.tryLangParse(token: String): Quester<out Any?>? {
            return topPrefixParsers.firstOrNull { it.canParseWith(this, token) }?.parseWith(this, token)
        }
    }
}