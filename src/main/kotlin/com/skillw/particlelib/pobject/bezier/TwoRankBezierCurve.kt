package com.skillw.particlelib.pobject.bezier

import taboolib.common.util.Location
import com.skillw.particlelib.pobject.ParticleObject
import java.util.function.Consumer

/**
 * 表示一条二阶贝塞尔曲线
 *
 * 给定三点, 自动生成一条二阶贝塞尔曲线
 *
 * 构造一个三阶贝塞尔曲线
 *
 * @author Zoyn
 */
class TwoRankBezierCurve
/**
 * @param p0 连续点
 * @param p1 控制点
 * @param p2 控制点
 * @param step 每个粒子的间隔(也即步长)
 * @constructor
 */
constructor(
    p0: Location,
    p1: Location,
    p2: Location,
    step: Double = 0.05,
) : ParticleObject(p0) {
    private val locations: MutableList<Location> = ArrayList()
    var p0: Location = p0
        set(value) {
            field = value
            resetLocations()
        }
    var p1: Location = p1
        set(value) {
            field = value
            resetLocations()
        }
    var p2: Location = p2
        set(value) {
            field = value
            resetLocations()
        }
    var step: Double = step
        set(value) {
            field = value
            resetLocations()
        }

    /**  */
    init {
        resetLocations()
    }

    override fun show() {
        locations.forEach(Consumer { loc: Location? -> loc?.let { spawnParticle(it) } })
    }

    /** 重新计算贝塞尔曲线上的点 */
    fun resetLocations() {
        locations.clear()
        // 算法
        // 算了我知道很蠢这个算法...
        var t = 0.0
        while (t < 1) {
            val v1 = p1.clone().subtract(p0).toVector()
            val t1 = p0.clone().add(v1.multiply(t))
            val v2 = p2.clone().subtract(p1).toVector()
            val t2 = p1.clone().add(v2.multiply(t))
            val v3 = t2.clone().subtract(t1).toVector()
            val destination = t1.clone().add(v3.multiply(t))
            locations.add(destination.clone())
            t += step
        }
    }
}