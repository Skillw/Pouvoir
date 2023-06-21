package com.skillw.asahi.internal.namespacing.prefix.lang.util

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest

/**
 * @className Logger
 *
 * @author Glom
 * @date 2023/1/14 0:22 Copyright 2023 user. All rights reserved.
 */
@AsahiPrefix(["print", "info"], "lang")
fun info() = prefixParser {
    //开始此函数的"编译"(parse)
    val content = quest<Any>()  //寻求一个任意类型对象
    // result里是执行函数时，要干的事情
    result {
        content.get().also {
            //打印它
            println(it)
        }
    }
}

@AsahiPrefix(["warning", "warn"], "lang")
fun warning() = prefixParser {
    val content = quest<Any>()
    result {
        content.get().also {
            taboolib.common.platform.function.warning(it)
        }
    }
}

@AsahiPrefix(["error"], "lang")
fun error() = prefixParser {
    val content = quest<Any>()
    result {
        content.get().also {
            exit()
            error(content.get().toString())
        }
    }
}