package com.skillw.particlelib.pobject

import taboolib.common.platform.function.submit
import taboolib.common.util.Location
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

/**
 * 表示一个星形线
 *
 * @author Zoyn IceCold
 */
class Astroid(var radius: Double, origin: Location, var step: Double) : ParticleObject(origin),
    Playable {
    private var currentT = 0.0

    /**
     * 构造一个星形线
     *
     * @param origin 原点
     */
    constructor(origin: Location) : this(1.0, origin)

    /**
     * 构造一个星形线
     *
     * @param radius 半径
     * @param origin 原点
     */
    constructor(radius: Double, origin: Location) : this(1.0, origin, 10.0)


    override fun show() {
        var t = 0.0
        while (t < 360.0) {
            val radians = Math.toRadians(t)
            // 计算公式
            val x = (radius * cos(radians)).pow(3.0)
            val z = (radius * sin(radians)).pow(3.0)
            spawnParticle(origin.clone().add(x, 0.0, z))
            t += step
        }
    }

    override fun play() {
        submit(period = period) {
            // 重置
            if (currentT > 360.0) {
                cancel()
                return@submit
            }
            once()
        }
    }

    override fun playNextPoint() {
        once()
        // 重置
        if (currentT > 360.0) {
            currentT = 0.0
        }
    }

    private fun once() {
        currentT += step
        val radians = Math.toRadians(currentT)
        // 计算公式
        val x = (radius * cos(radians)).pow(3.0)
        val z = (radius * sin(radians)).pow(3.0)
        spawnParticle(origin.clone().add(x, 0.0, z))
    }
}