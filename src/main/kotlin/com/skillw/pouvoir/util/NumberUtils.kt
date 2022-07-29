package com.skillw.pouvoir.util

import com.skillw.pouvoir.internal.manager.PouConfig
import java.text.DecimalFormat

// For script coders
object NumberUtils {
    @JvmOverloads
    @JvmStatic
    fun Number.format(input: String = PouConfig.numberFormat): String {
        val decimalFormat = DecimalFormat(input)
        return decimalFormat.format(this)
    }


    @JvmStatic
    fun randomInt(a: Int, b: Int): Int {
        return taboolib.common.util.random(a, b)
    }

    @JvmStatic
    fun random(a: Number, b: Number, format: String = PouConfig.numberFormat): String {
        return taboolib.common.util.random(a.toDouble(), b.toDouble()).format(format)
    }
}