package com.skillw.particlelib.pobject.equation

import taboolib.common.platform.function.submit
import taboolib.common.util.Location
import com.skillw.particlelib.pobject.ParticleObject
import com.skillw.particlelib.pobject.Playable
import java.util.function.Function

/**
 * 表示一个参数方程渲染器
 *
 * @param origin 原点
 * @param xFunction x函数
 * @param yFunction y函数
 * @param zFunction z函数
 * @param minT 自变量最小值
 * @param maxT 自变量最大值
 * @param deltaT 每次自变量所增加的量
 * @author Zoyn
 */
class ParametricEquationRenderer constructor(
    origin: Location,
    val xFunction: Function<Double, Double>,
    val yFunction: Function<Double, Double>,
    val zFunction: Function<Double, Double> = Function { 0.0 },
    var minT: Double = 0.0,
    var maxT: Double = 360.0,
    var deltaT: Double = 1.0,
) : ParticleObject(origin), Playable {
    private var currentT = 0.0

    /**
     * 参数方程渲染器, 自动将z方程变为0
     *
     * @param origin 原点
     * @param xFunction x函数
     * @param yFunction y函数
     */
    constructor(
        origin: Location,
        xFunction: Function<Double, Double>,
        yFunction: Function<Double, Double>,
        minT: Double = 0.0,
        maxT: Double = 360.0,
        dT: Double = 1.0,
    )
            : this(origin, xFunction, yFunction, { 0.0 }, minT, maxT, dT)


    override fun show() {
        var t = minT
        while (t < maxT) {
            val x = xFunction.apply(t)
            val y = yFunction.apply(t)
            val z = zFunction.apply(t)
            spawnParticle(origin.clone().add(x, y, z))
            t += deltaT
        }
    }

    override fun play() {
        submit(period = period) {
            // 进行关闭
            if (currentT > maxT) {
                cancel()
                return@submit
            }
            currentT += deltaT
            val x = xFunction.apply(currentT)
            val y = yFunction.apply(currentT)
            val z = zFunction.apply(currentT)
            spawnParticle(origin.clone().add(x, y, z))
        }
    }

    override fun playNextPoint() {
        if (currentT > maxT) {
            currentT = minT
        }
        currentT += deltaT
        val x = xFunction.apply(currentT)
        val y = yFunction.apply(currentT)
        val z = zFunction.apply(currentT)
        spawnParticle(origin.clone().add(x, y, z))
    }
}