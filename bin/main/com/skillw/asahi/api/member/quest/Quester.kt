package com.skillw.asahi.api.member.quest

import com.skillw.asahi.api.member.context.AsahiContext

fun interface Quester<R> {
    /**
     * 在上下文中执行/获取
     *
     * @return R
     * @receiver AsahiContext
     */
    fun AsahiContext.execute(): R

    fun run(context: AsahiContext): R {
        return context.run { execute() }
    }

    fun get(context: AsahiContext): R {
        return context.execute()
    }

}