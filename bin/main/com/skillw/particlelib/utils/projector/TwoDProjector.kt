package com.skillw.particlelib.utils.projector

import taboolib.common.util.Location
import taboolib.common.util.Vector
import java.util.function.BiFunction

/**
 * 表示一个二维至三维投影器
 *
 * 算法由 @Bryan33 提供
 *
 * @param origin 投影的原点
 * @param n 投影屏幕的法向量
 * @author Zoyn
 * @since 2020/9/19
 */
class TwoDProjector(private val origin: Location, n: Vector) {
    private val n1: Vector
    private val n2: Vector

    init {
        val t = n.clone()
        t.y = t.y + 1
        n1 = n.clone().crossProduct(t).normalize()
        n2 = n1.clone().crossProduct(n).normalize()
    }

    fun apply(x: Double, y: Double): Location {
        val r = n1.clone().multiply(x).add(n2.clone().multiply(y))
        return origin.clone().add(r)
    }

    companion object {
        /**
         * 创建二维至三维投影器 此方法返回的是BiFunction, 可以不用直接调用构造器
         *
         * @param loc 投影的原点
         * @param n 投影屏幕的法向量
         * @return [BiFunction]
         */
        fun create2DProjector(loc: Location, n: Vector): BiFunction<Double, Double, Location> {
            val t = n.clone()
            t.y = t.y + 1
            val n1 = n.clone().crossProduct(t).normalize()
            val n2 = n1.clone().crossProduct(n).normalize()
            return BiFunction { x: Double?, y: Double? ->
                val r = n1.clone().multiply(x!!).add(
                    n2.clone().multiply(
                        y!!
                    )
                )
                loc.clone().add(r)
            }
        }
    }
}