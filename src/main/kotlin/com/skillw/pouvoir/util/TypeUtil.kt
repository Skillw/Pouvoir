package com.skillw.pouvoir.util

import taboolib.common5.Coerce

fun String.cast(): Any {
    return when {
        startsWith("(int) ") -> Coerce.toInteger(substring(6))
        startsWith("(long) ") -> Coerce.toLong(substring(7))
        startsWith("(float) ") -> Coerce.toFloat(substring(8))
        startsWith("(double) ") -> Coerce.toDouble(substring(9))
        startsWith("(boolean) ") -> Coerce.toBoolean(substring(10))
        startsWith("(char) ") -> Coerce.toChar(substring(7))
        startsWith("(byte) ") -> Coerce.toByte(substring(7))
        startsWith("(short) ") -> Coerce.toShort(substring(8))
        else -> this
    }
}


fun Any.toTypeString(): Any {
    return when (this) {
        is Int -> "(int) $this"
        is Long -> "(long) $this"
        is Float -> "(float) $this"
        is Double -> "(double) $this"
        is Boolean -> "(boolean) $this"
        is Char -> "(char) $this"
        is Byte -> "(byte) $this"
        is Short -> "(short) $this"
        is Map<*, *> -> valuesToTypeString()
        is List<*> -> valuesToTypeString()
        is ByteArray -> map { it.toTypeString() }
        is IntArray -> map { it.toTypeString() }
        else -> this.toString()
    }
}


fun List<*>.valuesToTypeString(): List<Any> {
    return map { it?.toTypeString() ?: "null" }
}


fun Map<*, *>.valuesToTypeString(): Map<String, Any> {
    return mapKeys {
        it.key.toString()
    }.mapValues { (_, value) ->
        value?.toTypeString() ?: "null"
    }
}

fun MutableMap<String, Any?>.filterNonNull(): MutableMap<String, Any> {
    return entries.filter { it.value != null }.associate { Pair(it.key, it.value!!) }.toMutableMap()
}