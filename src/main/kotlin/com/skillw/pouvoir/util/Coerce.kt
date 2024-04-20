package com.skillw.pouvoir.util

import taboolib.common5.Coerce

//给JS用的
object Coerce {
    @JvmStatic
    fun toDouble(any: Any) {
        Coerce.toDouble(any)
    }
    @JvmStatic
    fun <T: Enum<T>> toEnum(any: Any, enum: Class<T>) {
        Coerce.toEnum(any, enum)
    }

    @JvmStatic
    fun toBoolean(any: Any) {
        Coerce.toBoolean(any)
    }
    @JvmStatic
    fun toString(any: Any) {
        Coerce.toString(any)
    }
    @JvmStatic
    fun toList(any: Any) {
        Coerce.toList(any)
    }

    @JvmStatic
    fun toFloat(any: Any) {
        Coerce.toFloat(any)
    }
    @JvmStatic
    fun toInteger(any: Any) {
        Coerce.toInteger(any)
    }

    @JvmStatic
    fun toLong(any: Any) {
        Coerce.toLong(any)
    }

    @JvmStatic
    fun toShort(any: Any) {
        Coerce.toShort(any)
    }

    @JvmStatic
    fun toChar(any: Any) {
        Coerce.toChar(any)
    }

    @JvmStatic
    fun toByte(any: Any) {
        Coerce.toByte(any)
    }
}