package com.skillw.asahi.internal.namespacing.prefix.lang.math

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest

/**
 * @className Math
 *
 * @author Glom
 * @date 2023/1/23 19:19 Copyright 2023 user. All rights reserved.
 */


@AsahiPrefix(["sin"], "lang")
private fun sin() = prefixParser {
    //弧度制
    val radians = quest<Double>()
    result { kotlin.math.sin(radians.get()) }
}

@AsahiPrefix(["cos"], "lang")
private fun cos() = prefixParser {
    //弧度制
    val radians = quest<Double>()
    result { kotlin.math.cos(radians.get()) }
}

@AsahiPrefix(["tan"], "lang")
private fun tan() = prefixParser {
    //弧度制
    val radians = quest<Double>()
    result { kotlin.math.tan(radians.get()) }
}

@AsahiPrefix(["asin"], "lang")
private fun asin() = prefixParser {
    val radians = quest<Double>()
    //返回弧度制
    result { kotlin.math.asin(radians.get()) }
}

@AsahiPrefix(["acos"], "lang")
private fun acos() = prefixParser {
    val radians = quest<Double>()
    //返回弧度制
    result { kotlin.math.cos(radians.get()) }
}

@AsahiPrefix(["atan"], "lang")
private fun atan() = prefixParser {
    val radians = quest<Double>()
    //返回弧度制
    result { kotlin.math.atan(radians.get()) }
}

/**
 * Computes the logarithm of the value x to the given base.
 *
 * @return PrefixCreator<Double>
 */
@AsahiPrefix(["log"], "lang")
private fun log() = prefixParser {
    val x = quest<Double>()
    val base = quest<Double>()
    result { kotlin.math.log(x.get(), base.get()) }
}

@AsahiPrefix(["log10", "lg"], "lang")
private fun log10() = prefixParser {
    val x = quest<Double>()
    result { kotlin.math.log10(x.get()) }
}

@AsahiPrefix(["ln"], "lang")
private fun ln() = prefixParser {
    val x = quest<Double>()
    result { kotlin.math.ln(x.get()) }
}

@AsahiPrefix(["log2"], "lang")
private fun log2() = prefixParser {
    val x = quest<Double>()
    result { kotlin.math.log2(x.get()) }
}