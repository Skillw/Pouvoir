package com.skillw.particlelib.pobject.bezier

import taboolib.common.util.Location
import com.skillw.particlelib.pobject.ParticleObject
import java.util.*

/**
 * 表示一条n阶贝塞尔曲线
 *
 * 给定 n + 1 点, 绘制一条平滑的曲线
 *
 * @author Zoyn
 */
class NRankBezierCurve
/**
 * 构造n阶贝塞尔曲线
 *
 * @param locations 所有的点
 * @param step T的步进数
 */
constructor(
    /** 用于计算贝塞尔曲线上的点 */
    private val locations: List<Location>,
    private val step: Double = 0.05,
) : ParticleObject(locations.first()) {
    /** 用于保存将要播放的粒子的点位 */
    private val points: MutableList<Location> = LinkedList()

    constructor(vararg locations: Location?) : this(Arrays.asList<Location>(*locations))

    /** 构造一个N阶贝塞尔曲线 */
    init {
        resetLocation()
    }

    override fun show() {
        points.forEach { loc ->
            this.spawnParticle(loc)
        }
    }

    fun resetLocation() {
        points.clear()
        var t = 0.0
        while (t < 1) {
            val location = calculateCurve(locations, t)
            points.add(location)
            t += step
        }
    }

    companion object {
        private fun calculateCurve(locList: List<Location>, t: Double): Location {
            if (locList.size == 2) {
                return locList[0].clone().add(locList[1].clone().subtract(locList[0]).toVector().multiply(t))
            }
            val locListTemp = ArrayList<Location>()
            for (i in locList.indices) {
                if (i + 1 == locList.size) {
                    break
                }
                val p0 = locList[i]
                val p1 = locList[i + 1]

                // 降阶处理
                locListTemp.add(p0.clone().add(p1.clone().subtract(p0).toVector().multiply(t)))
            }
            return calculateCurve(locListTemp, t)
        }
    }
}