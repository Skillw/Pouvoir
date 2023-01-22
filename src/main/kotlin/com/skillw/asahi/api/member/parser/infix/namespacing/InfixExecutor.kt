package com.skillw.asahi.api.member.parser.infix.namespacing

import com.skillw.asahi.api.member.context.InfixContext


/**
 * @className InfixExecutor
 *
 * @author Glom
 * @date 2022年12月27日 8:36 Copyright 2022 user. All rights reserved.
 */
fun interface InfixExecutor<T : Any> {
    /**
     * 执行中缀解释器执行内容
     *
     * @param obj 对象
     * @return 结果
     */
    fun InfixContext.execute(obj: T): Any?

    /**
     * 执行中缀解释器执行内容
     *
     * @param context 上下文
     * @param obj 对象
     * @return 结果
     */
    @Suppress("UNCHECKED_CAST")
    fun run(context: InfixContext, obj: Any): Any? {
        return context.run { execute(obj as? T ?: return null) }
    }
}