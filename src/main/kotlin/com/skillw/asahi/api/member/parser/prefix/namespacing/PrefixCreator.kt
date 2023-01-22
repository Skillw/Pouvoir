package com.skillw.asahi.api.member.parser.prefix.namespacing

import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.parser.prefix.namespacing.PrefixParser.Companion.prefixParser
import com.skillw.asahi.api.member.quest.Quester

/**
 * @className PrefixCreator
 *
 * 用来快速创建前缀解释器的接口
 *
 * @author Glom
 * @date 2023/1/14 11:07 Copyright 2023 user. All rights reserved.
 */
interface PrefixCreator<R> {
    /**
     * 解释器执行内容
     *
     * @return 结果
     */
    fun PrefixParser<R>.parse(): Quester<R>


    /**
     * 向命名空间注册前缀解释器
     *
     * @param key 名字
     * @param alias 名字
     * @param namespace 命名空间
     */
    fun register(
        key: String,
        vararg alias: String,
        namespace: String = "common",
    ) {
        object : BasePrefix<R>(key, *alias, namespace = namespace) {
            override fun AsahiLexer.parse(): Quester<R> {
                return this@PrefixCreator.run { prefixParser<R>().parse() }
            }
        }.register()
    }
}