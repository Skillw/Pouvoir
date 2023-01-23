package com.skillw.asahi.internal.namespacing.prefix.lang

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.lazyQuester
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest

/**
 * @className Variable
 *
 * @author Glom
 * @date 2023/1/13 19:09 Copyright 2023 user. All rights reserved.
 */
@AsahiPrefix(["set"], "lang")
private fun set() = prefixParser {
    val key = next()
    when {
        expect("ifndef") -> {
            result { if (containsKey(key)) return@result context()[key] else null }
        }

        expect("by") && expect("lazy") -> {
            expect("to", "=");

            val block = quest<Quester<*>>()
            result {
                lazyQuester {
                    block.get().also { result -> context()[key] = result }
                }.also { context()[key] = it }
            }
        }

        else -> {
            expect("to", "=")
            val value = quest<Any?>()
            result {
                value.get()?.let {
                    context()[key] = it;
                } ?: context().remove(key)
            }
        }
    }
}

@AsahiPrefix(["has"], "lang")
private fun has() = prefixParser {
    val key = quest<String>()
    result { containsKey(key.get()) }
}

@AsahiPrefix(["get"], "lang")
private fun get() = prefixParser {
    val key = quest<String>()
    result { get(key.get()) }
}

@AsahiPrefix(["delete"], "lang")
private fun delete() = prefixParser {
    val key = quest<String>()
    result { remove(key.get()) }
}