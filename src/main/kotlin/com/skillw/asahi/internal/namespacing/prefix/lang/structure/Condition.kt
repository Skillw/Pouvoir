package com.skillw.asahi.internal.namespacing.prefix.lang.structure

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester

/**
 * @className Condition
 *
 * @author Glom
 * @date 2023/1/14 0:21 Copyright 2024 Glom. 
 */

@AsahiPrefix(["condition"], "lang")
private fun condition() = prefixParser<Boolean> {
    val condition = questCondition(";")
    result {
        condition.get()
    }
}

//单路
@AsahiPrefix(["if"], "lang")
private fun `if`() = prefixParser<Any?> {
    val condition = questCondition("then")
    expect("then")
    val ifTrue = quest<Any?>()
    val ifFalse = if (expect("else")) quest<Any?>() else quester { }
    result {
        if (condition.get()) ifTrue.run()
        else ifFalse.run()
    }
}

//多路
@AsahiPrefix(["when", "switch"], "lang")
private fun `when`() = prefixParser<Any?> {
    val value = if (expect("of")) quest<Any?>() else null
    val pairs = ArrayList<Pair<Quester<Boolean>, Quester<Any?>>>()
    expect("{")
    while (expect("case", "when")) {
        value?.let {
            val condition = questCondition("->") {
                val symbol = next()
                val other = quest<Any?>()
                quester { com.skillw.asahi.util.check(value.get(), symbol, other.get()) }
            }
            expect("->")
            pairs.add(condition to quest())
        } ?: kotlin.run {
            val condition = questCondition("->")
            expect("->")
            pairs.add(condition to quest())
        }
    }
    if (expect("else", "default")) {
        expect("->")
        pairs.add(quester { true } to quest())
    }
    expect("}")
    result {
        pairs.forEach {
            if (it.first.get()) {
                return@result it.second.get()
            }
        }
    }
}