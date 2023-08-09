package com.skillw.pouvoir.util

import com.skillw.pouvoir.Pouvoir
import org.bukkit.entity.LivingEntity

/**
 * 计算工具类
 *
 * @constructor Create empty Calculation utils
 */


fun String.calculate(entity: LivingEntity? = null): Double {
    return calculate(Pouvoir.placeholderManager.replace(entity, this))
}


fun String.calculateDouble(entity: LivingEntity? = null): Double {
    return calculate(entity)
}


fun String.calculateInline(entity: LivingEntity? = null): String {
    var previousChar = 'a'
    val replacement = HashMap<IntRange, String>()
    var start = -1
    forEachIndexed { index, char ->
        if (previousChar == '{' && char == '{') {
            start = index - 1
        }
        if (previousChar == '}' && char == '}') {
            if (start == -1) return@forEachIndexed
            replacement += start..index to this.substring(start + 2, index - 1).calculate(entity).toString()
            start = -1
        }
        previousChar = char
    }
    return replacementIntRange(replacement)
}


fun calculate(input: String): Double {
    return input.calculate()
}


