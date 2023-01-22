package com.skillw.pouvoir.internal.feature.handler

import com.skillw.asahi.api.script.linking.Invoker
import com.skillw.asahi.api.script.linking.NativeFunction
import com.skillw.pouvoir.internal.core.asahi.linking.js.NativeJSFunction

/**
 * @className Native
 *
 * @author Glom
 * @date 2023/1/22 22:00 Copyright 2023 user. All rights reserved.
 */

internal fun Any.toInvoker(key: String, namespaces: Collection<String>): Invoker? {
    return when (this) {
        is Map<*, *> -> {
            val func = this as Map<String, Any>
            when (func["type"].toString()) {
                "js" -> NativeJSFunction.deserialize(key, func)
                else -> NativeFunction.deserialize(key, func, *namespaces.toTypedArray())
            }
        }

        is String -> {
            NativeFunction.deserialize(key, this, *namespaces.toTypedArray())
        }

        else -> null
    }
}