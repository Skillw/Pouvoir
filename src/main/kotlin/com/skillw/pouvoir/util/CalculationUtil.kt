package com.skillw.pouvoir.util

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.util.calculate.calculate
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


fun calculate(input: String): Double {
    return input.calculate()
}


