package com.skillw.particlelib.pobject.equation

import taboolib.common.platform.function.submit
import taboolib.common.util.Location
import com.skillw.particlelib.pobject.ParticleObject
import com.skillw.particlelib.pobject.Playable
import java.util.function.Function
import kotlin.math.cos
import kotlin.math.sin

/**
 * 表示一个极坐标方程渲染器
 *
 * @param origin 原点
 * @param function 极坐标方程
 * @param minTheta 自变量最小值
 * @param maxTheta 自变量最大值
 * @param dTheta 每次自变量所增加的量
 * @author Zoyn
 */
class PolarEquationRenderer constructor(
    origin: Location,
    val function: Function<Double, Double>,
    var minTheta: Double = 0.0,
    var maxTheta: Double = 360.0,
    var dTheta: Double = 1.0,
) : ParticleObject(origin), Playable {
    private var currentTheta = 0.0
    override fun show() {
        var theta = minTheta
        while (theta < maxTheta) {
            val rho = function.apply(theta)
            val x = rho * cos(theta)
            val y = rho * sin(theta)
            spawnParticle(origin.clone().add(x, y, 0.0))
            theta += dTheta
        }
    }

    override fun play() {
        submit(period = period) {
            // 进行关闭
            if (currentTheta > maxTheta) {
                cancel()
                return@submit
            }
            currentTheta += dTheta
            val rho = function.apply(currentTheta)
            val x = rho * cos(currentTheta)
            val y = rho * sin(currentTheta)
            spawnParticle(origin.clone().add(x, y, 0.0))
        }
    }

    override fun playNextPoint() {
        // 进行关闭
        if (currentTheta > maxTheta) {
            currentTheta = minTheta
        }
        currentTheta += dTheta
        val rho = function.apply(currentTheta)
        val x = rho * cos(currentTheta)
        val y = rho * sin(currentTheta)
        spawnParticle(origin.clone().add(x, y, 0.0))
    }
}