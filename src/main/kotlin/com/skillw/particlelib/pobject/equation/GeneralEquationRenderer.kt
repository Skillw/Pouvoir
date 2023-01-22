package com.skillw.particlelib.pobject.equation

import taboolib.common.platform.function.submit
import taboolib.common.util.Location
import com.skillw.particlelib.pobject.ParticleObject
import com.skillw.particlelib.pobject.Playable
import java.util.function.Function

/**
 * 表示一个普通方程渲染器
 *
 * @author Zoyn
 */
class GeneralEquationRenderer constructor(
    origin: Location,
    /** 函数 */
    val function: Function<Double, Double>,
    /** 自变量最小值 */
    var minX: Double = -5.0,
    /** 自变量最大值 */
    var maxX: Double = 5.0,
    /** 自变量变化值 */
    var deltaX: Double = 0.1,
) : ParticleObject(origin), Playable {
    private var currentX = 0.0

    override fun show() {
        var x = minX
        while (x < maxX) {
            spawnParticle(origin.clone().add(x, function.apply(x), 0.0))
            x += deltaX
        }
    }

    override fun play() {
        submit(period = period) {
            // 进行关闭
            if (currentX > maxX) {
                cancel()
                return@submit
            }
            currentX += deltaX
            spawnParticle(origin.clone().add(currentX, function.apply(currentX), 0.0))
        }
    }

    override fun playNextPoint() {
        // 进行关闭
        if (currentX > maxX) {
            currentX = minX
        }
        currentX += deltaX
        spawnParticle(origin.clone().add(currentX, function.apply(currentX), 0.0))
    }
}