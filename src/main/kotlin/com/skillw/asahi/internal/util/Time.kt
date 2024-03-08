package com.skillw.asahi.internal.util

import taboolib.common5.util.parseMillis

/**
 * @className Time
 *
 * @author Glom
 * @date 2023/1/11 23:25 Copyright 2024 Glom.
 */
class Time(var millis: Long) {
    constructor(string: String) : this(string.parseMillis())

    fun toTick(): Long = millis / 50

    companion object {
        @JvmStatic
        val noTime = Time(-1)

        @JvmStatic
        fun tick(tick: Long): Time {
            return Time(tick * 50)
        }
    }
}