package com.skillw.particlelib.pobject.equation

import java.util.function.Function
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

object Equations {
    /** 二次函数 */
    val QUADRATIC_FUNCTION = Function { x: Double ->
        (x).pow(2.0)
    }

    /** 一次函数 */
    val LINEAR_FUNCTION = Function { x: Double -> x }

    /** cos函数 */
    val COS_FUNCTION = Function { a: Double -> cos(a) }

    /** sin函数 */
    val SIN_FUNCTION = Function { a: Double -> sin(a) }

    /** 极坐标:四叶玫瑰线 */
    val POLAR_FOUR_LEAVE_CURVE = Function { theta: Double -> 1.5 * sin(2 * theta) }
}