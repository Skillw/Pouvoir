package com.skillw.asahi.internal.namespacing.prefix.lang.math

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.util.cast
import com.skillw.pouvoir.util.format
import kotlin.math.absoluteValue

/**
 * @className Number
 *
 * @author Glom
 * @date 2023/1/14 0:31 Copyright 2024 Glom.
 */


@AsahiPrefix(["abs"], "lang")
private fun abs() = prefixParser {
    val number = quest<Double>()
    result { number.get().absoluteValue }
}

@AsahiPrefix(["ceil"], "lang")
private fun ceil() = prefixParser {
    val number = quest<Double>()
    result { kotlin.math.ceil(number.get()) }
}

@AsahiPrefix(["floor"], "lang")
private fun floor() = prefixParser {
    val number = quest<Double>()
    result { kotlin.math.floor(number.get()) }
}

@AsahiPrefix(["format"], "lang")
private fun format() = prefixParser {
    val number = quest<Double>()
    val format = quest<String>()
    result { number.get().format(format.get()) }
}

@AsahiPrefix(["max"], "lang")
private fun max() = prefixParser {
    val value = if (peek() == "[")
        quest<List<Any?>>()
    else {
        val a = quest<Double>()
        expect("to")
        val b = quest<Double>()
        result { a.get() to b.get() }
    }
    result {
        when (val values = value.get()) {
            is List<*> -> {
                val numbers = values.map { it.cast<Double>() }
                numbers.maxOf { it }
            }

            is Pair<*, *> -> {
                val a = values.first.cast<Double>()
                val b = values.second.cast<Double>()
                kotlin.math.max(a, b)
            }

            else -> 0.0
        }
    }
}

@AsahiPrefix(["min"], "lang")
private fun min() = prefixParser {
    val value = if (peek() == "[")
        quest<List<Any?>>()
    else {
        val a = quest<Double>()
        expect("to")
        val b = quest<Double>()
        result { a.get() to b.get() }
    }
    result {
        when (val values = value.get()) {
            is List<*> -> {
                val numbers = values.map { it.cast<Double>() }
                numbers.minOf { it }
            }

            is Pair<*, *> -> {
                val a = values.first.cast<Double>()
                val b = values.second.cast<Double>()
                kotlin.math.min(a, b)
            }

            else -> 0.0
        }
    }
}

@AsahiPrefix(["round"], "lang")
private fun round() = prefixParser {
    val x = quest<Double>()
    result { kotlin.math.round(x.get()).toInt() }
}

@AsahiPrefix(["range"], "lang")
private fun range() = prefixParser {
    val from = quest<Double>()
    expect("to", "~", "..")
    val to = quest<Double>()
    result {
        val a = from.get()
        val b = to.get()
        a..b
    }
}

@AsahiPrefix(["number"], "lang")
private fun number() = prefixParser {
    val number = quest<Double>()
    result { number.get() }
}