package com.skillw.asahi.api.member.parser.prefix.namespacing

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.quest.Quester

/**
 * @className PrefixParser
 *
 * 前缀解释器的编译上下文
 *
 * @author Glom
 * @date 2022/12/27 19:31 Copyright 2022 user. All rights reserved.
 */
class PrefixParser<R> private constructor(
    reader: AsahiLexer,
) : AsahiLexer by reader {
    companion object {
        fun <R> AsahiLexer.prefixParser(): PrefixParser<R> {
            return PrefixParser(this)
        }
    }

    /**
     * 结果
     *
     * @param exec 结果内容
     * @param R 返回类型
     * @return 返回的结果
     * @receiver
     */
    fun <R> result(exec: AsahiContext.() -> R): Quester<R> {
        return Quester { exec() }
    }
}