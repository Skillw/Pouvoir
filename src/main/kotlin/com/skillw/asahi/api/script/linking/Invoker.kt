package com.skillw.asahi.api.script.linking

import com.skillw.asahi.api.member.context.AsahiContext

/**
 * @className Invoker
 *
 * @author Glom
 * @date 2023/1/22 19:00 Copyright 2024 Glom.
 */
fun interface Invoker {
    fun invoke(context: AsahiContext, vararg params: Any?): Any?
}