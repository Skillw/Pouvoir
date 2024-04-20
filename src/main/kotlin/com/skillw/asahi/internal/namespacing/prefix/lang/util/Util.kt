package com.skillw.asahi.internal.namespacing.prefix.lang.util

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import com.skillw.asahi.internal.util.MapTemplate
import com.skillw.pouvoir.util.Tick
import taboolib.common5.Coerce
import java.awt.Color
import java.text.SimpleDateFormat
import java.util.*

/**
 * @className Util
 *
 * @author Glom
 * @date 2023/1/14 0:45 Copyright 2024 Glom.
 */
@AsahiPrefix(["arrayOf"], "lang")
private fun arrayOf() = prefixParser<Any?> {
    val array = quest<Array<Any?>>()
    result { array.get() }
}

@AsahiPrefix(["listOf"], "lang")
private fun listOf() = prefixParser<Any?> {
    val list = quest<List<Any?>>()
    result { list.get() }
}

@AsahiPrefix(["setOf"], "lang")
private fun setOf() = prefixParser<Any?> {
    val set = quest<Set<Any?>>()
    result { set.get() }
}

@AsahiPrefix(["mapOf"], "lang")
private fun mapOf() = prefixParser<Any?> {
    if (expect("with")) {
        val template = quest<MapTemplate>()
        val list = quest<List<Any>>()
        result { template.get().build(list.get()) }
    } else {
        val map = quest<MutableMap<String, Any?>>()
        result { map.get() }
    }
}

@AsahiPrefix(["mapListOf"], "lang")
private fun mapListOf() = prefixParser<MutableList<MutableMap<String,Any>>> {
    expect("with")
    val templateGetter = quest<MapTemplate>()
    val list = quest<List<Any>>()
    result {
        val template = templateGetter.get()
        list.get().mapNotNull { template.build(it as? List<Any>? ?: return@mapNotNull null) }
            .toMutableList()
    }
}

@AsahiPrefix(["pair"], "lang")
private fun pair() = prefixParser<Pair<Any,Any>> {
    val pair = quest<Pair<Any, Any>>()
    result { pair.get() }
}

@AsahiPrefix(["mapTemplate"], "lang")
private fun mapTemplate() = prefixParser<MapTemplate> {
    val list = quest<List<Any>>()
    result { MapTemplate(list.get().map { it.toString() }) }
}

/**
 * @className Operator
 *
 * @author Glom
 * @date 2023/1/14 0:42 Copyright 2024 Glom.
 */
@AsahiPrefix(["join"], "lang")
private fun join() = prefixParser<String> {
    val list = quest<List<Any>>()
    val by = if (expect("by")) quest() else quester { "" }
    result {
        list.get().joinToString(by.get().replace("\\n", "\n"))
    }
}

@AsahiPrefix(["replace"], "lang")
private fun replace() = prefixParser<String> {
    val str = quest<String>()
    expect("with")
    val replacement = quest<Map<String, Any>>()
    result {
        var cache = str.get()
        replacement.get().mapValues { it.value.toString() }.forEach { (origin, new) ->
            cache = cache.replace(origin, new)
        }
        cache
    }
}

@AsahiPrefix(["type"], "lang")
fun type() = prefixParser<Any?> {
    val type = quest<String>()
    val getter = quest<Any?>()
    result {
        val obj = getter.get()
        when (type.get()) {
            "double" -> Coerce.toDouble(obj)
            "int" -> Coerce.toInteger(obj)
            "long" -> Coerce.toLong(obj)
            "float" -> Coerce.toFloat(obj)
            "short" -> Coerce.toShort(obj)
            "byte" -> Coerce.toByte(obj)
            "bool" -> Coerce.toBoolean(obj)
            "char" -> Coerce.toChar(obj)
            "string" -> Coerce.toString(obj)
            else -> obj
        }
    }
}

@AsahiPrefix(["select", "with"], "lang")
private fun with() = prefixParser<Any> {
    val any = quest<Any>()
    val exec = parseScript()
    result {
        context().clone().run {
            when (val obj = any.get()) {
                is Collection<*> -> {
                    obj.filterNotNull().forEach {
                        select(it)
                        exec.run()
                    }
                }

                else -> {
                    select(obj)
                    exec.run()
                }
            }
            this@result.putAllIfExists(this)
        }
    }
}

@AsahiPrefix(["date"], "lang")
private fun date() = prefixParser<String> {
    val typeGetter = if (expect("in")) questString() else quester { null }
    result {
        val date = Date()
        SimpleDateFormat(
            when (typeGetter.get()?.lowercase()) {
                "year" -> "yyyy"
                "month" -> "MM"
                "day" -> "dd"
                "time" -> "HH:mm:ss"
                "timeDetail" -> "HH:mm:ss.SSS"
                else -> "yyyy年MM月dd日 HH:mm:ss"
            }
        ).format(date)
    }
}


@AsahiPrefix(["time"], "lang")
private fun time() = prefixParser<String> {
    val formatGetter = if (expect("as")) questString() else quester { "yyyy-MM-dd HH:mm:ss" }
    result {
        SimpleDateFormat(formatGetter.get()).format(Date())
    }
}

@AsahiPrefix(["color"])
private fun color() = prefixParser<Color> {
    expect("[")
    val r = quest<Int>()
    val g = quest<Int>()
    val b = quest<Int>()
    expect("]")
    result { Color(r.get(), g.get(), b.get()) }
}


@AsahiPrefix(["currentTick"], "lang")
private fun currentTick() = prefixParser<Long> {
    result {
        Tick.currentTick
    }
}