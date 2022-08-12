package com.skillw.pouvoir.internal.function

import com.skillw.pouvoir.Pouvoir.pouFunctionManager
import com.skillw.pouvoir.api.function.context.IContext
import com.skillw.pouvoir.internal.function.context.SimpleContext

/**
 * @className FileReader
 *
 * @author Glom
 * @date 2022/8/1 8:59 Copyright 2022 user. All rights reserved.
 */
object TextHandler {
    //    while (hasNext()) {
//        if (next()?.contains("{") == true) {
//            var count = 0
//            println(current())
//            val origin = StringBuilder(current().substringAfter("{"))
//            println(origin)
//            var next: String
//            inner@ while (hasNext()) {
//                next = next() ?: return@with
//                when {
//                    next.contains("{") -> {
//                        count++
//                    }
//
//                    next.contains("}") -> {
//                        if (--count <= 0) {
//                            origin.append(" " + next.substringBefore("}"))
//                            break@inner
//                        }
//                    }
//                }
//                origin.append(" $next")
//            }
//            val originStr = origin.toString()
//            val replaced =
//                pouFunctionManager.parse(
//                    SimpleReader(originStr),
//                    context = global
//                ).toString()
//            result = result.replaceFirst("{$originStr}", replaced)
//        }
//    }
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
                        pouFunctionManager.parse(
                            origin,
                            context = global
                        ).toString()
                    result = result.replaceFirst("{$origin}", replaced)
                    start = -1
                } else ignore = false

                else -> ignore = false
            }
        }
        return result
    }
}