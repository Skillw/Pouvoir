package com.skillw.pouvoir.util

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.internal.manager.PouConfig
import com.skillw.pouvoir.util.calculate.calculate
import org.bukkit.entity.LivingEntity
import java.math.BigDecimal

/**
 * 计算工具类
 *
 * @constructor Create empty Calculation utils
 */


fun String.calculate(entity: LivingEntity? = null): BigDecimal {
    return calculate(Pouvoir.placeholderManager.replace(entity, this))
}

fun String.calculateDouble(entity: LivingEntity? = null): Double {
    return calculate(entity).setScale(PouConfig.scale, BigDecimal.ROUND_HALF_UP).toDouble()
}


fun calculate(input: String): BigDecimal {
    return input.calculate()
}


