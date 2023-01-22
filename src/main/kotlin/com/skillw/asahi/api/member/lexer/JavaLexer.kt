package com.skillw.asahi.api.member.lexer

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.quest.Quester

/**
 * @className JavaFuncReader
 *
 * @author Glom
 * @date 2023/1/15 13:18 Copyright 2023 user. All rights reserved.
 */
class JavaLexer(reader: AsahiLexer) : AsahiLexer by reader {
    /**
     * 加了一层类型转换的quest
     *
     * @param R 类型
     * @return
     */
    fun <R> questAs(): Quester<R> {
        return questObj() as Quester<R>
    }

    /**
     * 结果
     *
     * @param exec 结果内容
     * @param R 返回类型
     * @return
     * @receiver
     */
    fun <R> result(exec: AsahiContext.() -> R): Quester<R> {
        return Quester { exec() }
    }
}