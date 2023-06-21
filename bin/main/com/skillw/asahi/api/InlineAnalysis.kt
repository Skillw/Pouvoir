package com.skillw.asahi.api

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.namespace.NamespaceHolder
import com.skillw.asahi.internal.lexer.InlineAnalysisImpl

/**
 * @className InlineReaderImpl
 *
 * @author Glom
 * @date 2023/1/14 10:04 Copyright 2023 user. All rights reserved.
 */
interface InlineAnalysis : NamespaceHolder<InlineAnalysis> {
    /**
     * 根据上下文解析内联文本
     *
     * @param context 上下文
     * @return 解析后的内联文本
     */
    fun analysis(context: AsahiContext = AsahiContext.create()): String

    companion object {
        /**
         * 创建内联文本解释器对象
         *
         * @param string String 文本
         * @return InlineAnalysis
         */
        @JvmStatic
        fun of(string: String): InlineAnalysis {
            return InlineAnalysisImpl.of(string)
        }
    }
}