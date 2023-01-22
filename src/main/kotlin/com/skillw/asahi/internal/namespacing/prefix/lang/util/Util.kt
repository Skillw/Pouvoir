package com.skillw.asahi.internal.namespacing.prefix.lang.util

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import com.skillw.asahi.internal.util.MapTemplate
import taboolib.common5.Coerce

/**
 * @className Util
 *
 * @author Glom
 * @date 2023/1/14 0:45 Copyright 2023 user. All rights reserved.
 */
@AsahiPrefix(["arrayOf"], "lang")
private fun arrayOf() = prefixParser {
    val array = quest<Array<Any?>>()
    result { array.get() }
}

@AsahiPrefix(["listOf"], "lang")
private fun listOf() = prefixParser {
    val list = quest<List<Any?>>()
    result { list.get() }
}

@AsahiPrefix(["setOf"], "lang")
private fun setOf() = prefixParser {
    val set = quest<Set<Any?>>()
    result { set.get() }
}

@AsahiPrefix(["mapOf"], "lang")
private fun mapOf() = prefixParser {
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
private fun mapListOf() = prefixParser {
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
private fun pair() = prefixParser {
    val pair = quest<Pair<Any, Any>>()
    result { pair.get() }
}

@AsahiPrefix(["mapTemplate"], "lang")
private fun mapTemplate() = prefixParser {
    val list = quest<List<Any>>()
    result { MapTemplate(list.get().map { it.toString() }) }
}

/**
 * @className Operator
 *
 * @author Glom
 * @date 2023/1/14 0:42 Copyright 2023 user. All rights reserved.
 */
@AsahiPrefix(["join"], "lang")
private fun join() = prefixParser {
    val list = quest<List<Any>>()
    val by = if (expect("by")) quest() else quester { "" }
    result {
        list.get().joinToString(by.get().replace("\\n", "\n"))
    }
}

@AsahiPrefix(["replace"], "lang")
private fun replace() = prefixParser {
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
fun type() = prefixParser {
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

@AsahiPrefix(["select"], "lang")
private fun select() = prefixParser {
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