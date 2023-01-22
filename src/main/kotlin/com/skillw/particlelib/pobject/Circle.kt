package com.skillw.particlelib.pobject

import taboolib.common.util.Location

/**
 * 表示一个圆
 *
 * @author Zoyn IceCold
 */
class Circle
/**
 * 构造一个圆
 *
 * @param origin 圆的圆点
 * @param radius 圆的半径
 * @param step 每个粒子的间隔(也即步长)
 * @param period 特效周期(如果需要可以使用)
 */(origin: Location, radius: Double = 1.0, step: Double = 1.0, period: Long = 20L) :
    Arc(origin, 0.0, 360.0, radius, step, period)