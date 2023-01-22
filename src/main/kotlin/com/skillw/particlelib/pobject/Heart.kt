package com.skillw.particlelib.pobject

import taboolib.common.platform.function.submit
import taboolib.common.util.Location
import kotlin.math.*

/**
 * 表示一颗心
 *
 * @param xScaleRate X轴缩放比率
 * @param yScaleRate Y轴缩放比率
 * @param origin 原点
 * @author Zoyn IceCold 构造一个心形线
 */
class Heart(origin: Location, var xScaleRate: Double = 1.0, var yScaleRate: Double = 1.0) :
    ParticleObject(origin),
    Playable {
    /** 表示步进的程度 */
    var step = 0.001
    private var currentT = -1.0

    override fun show() {
        var t = -1.0
        while (t <= 1.0) {
            val x = xScaleRate * sin(t) * cos(t) * ln(abs(t))
            val y = yScaleRate * sqrt(abs(t)) * cos(t)
            spawnParticle(origin.clone().add(x, 0.0, y))
            t += step
        }
    }

    override fun play() {
        submit(period = period) {
            if (currentT > 1.0) {
                cancel()
                return@submit
            }
            once()
        }
    }

    override fun playNextPoint() {
        once()
        if (currentT > 1.0) {
            currentT = -1.0
        }
    }

    private fun once() {
        currentT += step
        val x = xScaleRate * sin(currentT) * cos(currentT) * ln(abs(currentT))
        val y = yScaleRate * sqrt(abs(currentT)) * cos(currentT)
        spawnParticle(origin.clone().add(x, 0.0, y))
    }
}