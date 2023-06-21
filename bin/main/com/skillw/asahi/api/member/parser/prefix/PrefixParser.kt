package com.skillw.asahi.api.member.parser.prefix

import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.quest.Quester

/**
 * @className Parser
 *
 * @author Glom
 * @date 2023/1/15 13:14 Copyright 2023 user. All rights reserved.
 */
interface PrefixParser<R> {
    /**
     * 前缀解释器执行内容
     *
     * @param lexer 词法器
     * @return 结果
     */
    fun parseWith(lexer: AsahiLexer): Quester<R>?
}