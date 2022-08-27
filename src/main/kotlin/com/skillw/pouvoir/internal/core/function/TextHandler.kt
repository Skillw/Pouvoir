package com.skillw.pouvoir.internal.core.function

import com.skillw.pouvoir.Pouvoir.pouFunctionManager
import com.skillw.pouvoir.api.function.context.IContext
import com.skillw.pouvoir.internal.core.function.context.SimpleContext

/**
 * @className FileReader
 *
 * @author Glom
 * @date 2022/8/1 8:59 Copyright 2022 user. All rights reserved.
 */
object TextHandler {
    fun analysis(text: String, global: IContext = SimpleContext()): String {
        var result = text
        var ignore = false
        var start = -1
        var count = 0
        for (index in text.indices) {
            when (text[index]) {
                '\\' -> ignore = true
                '{' -> if (!ignore) {
                    if (start != -1) {
                        count++
                        continue
                    }
                    start = index
                } else ignore = false

                '}' -> if (!ignore) {
                    if (start == -1 || count-- > 0) continue
                    val origin = text.substring(start + 1, index)
                    val replaced =
                        pouFunctionManager.eval(
                            origin,
                            context = global
                        ).toString()
                    if (replaced == origin) return text
                    result = result.replaceFirst("{$origin}", replaced)
                    start = -1
                } else ignore = false

                else -> ignore = false
            }
        }
        return result
    }
}