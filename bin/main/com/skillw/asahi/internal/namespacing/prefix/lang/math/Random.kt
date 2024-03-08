package com.skillw.asahi.internal.namespacing.prefix.lang.math

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import taboolib.common5.RandomList
import java.util.*

/**
 * @className Random
 *
 * @author Glom
 * @date 2023/1/14 0:31 Copyright 2024 Glom.
 */

@AsahiPrefix(["random"], "lang")
private fun random() = prefixParser {
    val x = quest<Double>()
    expect("to")
    val y = quest<Double>()
    result { com.skillw.pouvoir.util.random(x.get(), y.get()) }
}

@AsahiPrefix(["randomInt"], "lang")
private fun randomInt() = prefixParser {
    val x = quest<Int>()
    expect("to")
    val y = quest<Int>()
    result { com.skillw.pouvoir.util.randomInt(x.get(), y.get()) }
}

@AsahiPrefix(["randomObj"], "lang")
private fun randomObj() = prefixParser {
    val list = quest<List<Any>>()
    result { list.get().random() }
}

@AsahiPrefix(["weight"], "lang")
private fun weight() = prefixParser {
    expect("[")
    val list = LinkedList<Pair<Quester<Int>, Quester<Any>>>()
    do {
        val weight = quest<Int>()
        expect("to", "=", ":")
        val value = quest<Any>()
        list += weight to value
        expect(",")
    } while (!expect("]"))
    val builder = peek() == "build"
    if (builder) next()
    result {
        val randomList = RandomList<Any>()
        list.map { it.second.get() to it.first.get() }.forEach { (value, weight) ->
            randomList.add(value, weight)
        }
        return@result if (!builder) randomList.random()!! else randomList
    }
}