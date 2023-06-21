package com.skillw.asahi.internal.namespacing.prefix.lang.util

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.util.cast

/**
 * @className Logic
 *
 * @author Glom
 * @date 2023/1/14 0:15 Copyright 2023 user. All rights reserved.
 */
@AsahiPrefix(["not"], "lang")
private fun not() = prefixParser {
    val bool = quest<Boolean>()
    result { !bool.get() }
}

@AsahiPrefix(["all"], "lang")
private fun all() = prefixParser {
    val array = quest<Array<Any?>>()
    result {
        array.get().all { it.cast() }
    }

}

@AsahiPrefix(["any"], "lang")
private fun any() = prefixParser {
    val array = quest<Array<Any?>>()
    result {
        array.get().any { it.cast() }
    }
}

@AsahiPrefix(["check", "?"], "lang")
private fun check() = prefixParser {
    val a = quest<Any>()
    val symbol = next()
    val b = quest<Any>()
    result {
        com.skillw.asahi.util.check(a.get(), symbol, b.get())
    }
}

