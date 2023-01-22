package com.skillw.particlelib.utils.projector

import taboolib.common.util.Location
import taboolib.common.util.Vector

/**
 * 表示一个三维至三维投影器
 *
 * 算法由 @Bryan33 提供
 *
 * @author Zoyn
 * @since 2020/9/19
 */
class ThreeDProjector(private val origin: Location, n: Vector) {
    private val n1: Vector
    private val n2: Vector
    private val n3: Vector

    /**
     * @param origin 投影的原点
     * @param n 投影屏幕的法向量
     */
    init {
        val t = n.clone()
        t.y = t.y + 1
        n1 = n.clone().crossProduct(t).normalize()
        n2 = n1.clone().crossProduct(n).normalize()
        n3 = n.clone().normalize()
    }

    fun apply(x: Double, y: Double, z: Double): Location {
        val r = n1.clone().multiply(x).add(n2.clone().multiply(z)).add(n3.clone().multiply(y))
        return origin.clone().add(r)
    }
}