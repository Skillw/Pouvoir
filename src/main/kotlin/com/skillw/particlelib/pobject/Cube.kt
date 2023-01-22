package com.skillw.particlelib.pobject

import taboolib.common.util.Location
import taboolib.common.util.Vector
import com.skillw.particlelib.utils.VectorUtils
import kotlin.math.max
import kotlin.math.min

/**
 * 表示一个立方体特效
 *
 * @author Zoyn
 */
class Cube constructor(
    private val minLoc: Location,
    private val maxLoc: Location,
    private val step: Double = 0.2,
) : ParticleObject(minLoc) {
    /**
     * 构造一个立方体
     *
     * @param minLoc 一个点
     * @param maxLoc 另外一个点
     * @param step 绘制边框时的步进长度
     */
    init {
        require(minLoc.world === maxLoc.world) { "这两个坐标的所对应的世界不相同" }
    }

    override fun show() {
        // 获得最大最小的两个点
        val minX = min(minLoc.x, maxLoc.x)
        val minY = min(minLoc.y, maxLoc.y)
        val minZ = min(minLoc.z, maxLoc.z)
        val maxX = max(minLoc.x, maxLoc.x)
        val maxY = max(minLoc.y, maxLoc.y)
        val maxZ = max(minLoc.z, maxLoc.z)
        val minLoc = Location(minLoc.world, minX, minY, minZ)

        // 获得立方体的 长 宽 高
        val width = maxX - minX
        val height = maxY - minY
        val depth = maxZ - minZ

        // 此处的 newOrigin是底部的四个点
        var newOrigin = minLoc
        var length: Double
        // 这里直接得到向X正半轴方向的向量
        var vector: Vector? = RIGHT.clone()
        for (i in 1..4) {
            length = if (i % 2 == 0) {
                depth
            } else {
                width
            }

            // 4条高
            run {
                var j = 0.0
                while (j < height) {
                    spawnParticle(newOrigin.clone().add(UP.clone().multiply(j)))
                    j += step
                }
            }

            // 第n条边
            var j = 0.0
            while (j < length) {
                val spawnLoc = newOrigin.clone().add(vector!!.clone().multiply(j))
                spawnParticle(spawnLoc)
                spawnParticle(spawnLoc.add(0.0, height, 0.0))
                j += step
            }
            // 获取结束时的坐标
            newOrigin = newOrigin.clone().add(vector!!.clone().multiply(length))
            vector = VectorUtils.rotateAroundAxisY(vector, 90.0)
        }
    }

    companion object {
        /** 向上的向量 */
        private val UP = Vector(0, 1, 0).normalize()

        /** 向 X正半轴 的向量 */
        private val RIGHT = Vector(1, 0, 0).normalize()
    }
}