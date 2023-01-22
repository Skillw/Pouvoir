package com.skillw.particlelib.pobject

import taboolib.common.platform.function.submit
import taboolib.common.util.Location
import kotlin.math.cos
import kotlin.math.sin

/**
 * 表示一个弧
 *
 * @param origin 弧所在的圆的圆点
 * @param startAngle 弧开始的角度
 * @param angle 弧所占的角度
 * @param radius 弧所在的圆的半径
 * @param step 每个粒子的间隔(也即步长)
 * @param period 特效周期(如果需要可以使用) /
 */
open class Arc constructor(
    origin: Location,
    var startAngle: Double = 0.0,
    var angle: Double = 0.0,
    var radius: Double = 1.0,
    var step: Double = 1.0,
    period: Long = 20L,
) : ParticleObject(origin, period), Playable {
    constructor(origin: Location, angle: Double = 60.0) : this(origin, 0.0, angle)

    private var currentAngle: Double = 0.0
    override fun show() {
        var i = startAngle
        while (i < angle) {
            once(i)
            i += step
        }
    }

    override fun play() {
        submit(period = period) {
            // 进行关闭
            if (currentAngle > angle) {
                cancel()
                return@submit
            }
            currentAngle += step
            once(currentAngle)
        }
    }

    private fun once(angle: Double) {
        val radians = Math.toRadians(angle)
        val x = radius * cos(radians)
        val z = radius * sin(radians)
        spawnParticle(origin.clone().add(x, 0.0, z))
    }

    override fun playNextPoint() {
        currentAngle += step
        once(currentAngle)

        // 进行重置
        if (currentAngle > angle) {
            currentAngle = 0.0
        }
    }
}