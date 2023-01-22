package com.skillw.pouvoir.util

import com.skillw.pouvoir.internal.manager.PouConfig
import java.text.DecimalFormat

// For script coders
fun Number.format(input: String = PouConfig.numberFormat): String {
    val decimalFormat = DecimalFormat(input)
    return decimalFormat.format(this)
}

fun randomInt(a: Int, b: Int): Int {
    return taboolib.common.util.random(a, b)
}

fun random(a: Number, b: Number, format: String = PouConfig.numberFormat): String {
    return taboolib.common.util.random(a.toDouble(), b.toDouble()).format(format)
}

private val romanNum = HashMap<Char, Int>().apply {
    put('I', 1);
    put('V', 5);
    put('X', 10);
    put('L', 50);
    put('C', 100);
    put('D', 500);
    put('M', 1000);
}

fun Char.romanInt(): Int {
    return romanNum[this] ?: 0
}


fun String.romanToInt(): Int {
    //数字累加
    var ans = 0;
    for (i in indices) {

        //取出罗马字母所对应的数字
        val value = this[i].romanInt()
        //从左往右，直接与下一位进行比较；
        //如果小于右边，直接减去
        if (i < (length - 1) && value < this[i + 1].romanInt()) {
            ans -= value;
        } else {
            ans += value;
        }
    }
    return ans;
}