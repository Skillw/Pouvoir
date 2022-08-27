package com.skillw.pouvoir.internal.core.script.common.hook

/**
 * @className Invoker
 *
 * @author Glom
 * @date 2022/7/29 21:51 Copyright 2022 user. All rights reserved.
 */
fun interface Invoker {
    fun invoke(
        function: String,
        arguments: Map<String, Any>,
        vararg parameters: Any?,
        receiver: MutableMap<String, Any>.() -> Unit,
    ): Any?
}