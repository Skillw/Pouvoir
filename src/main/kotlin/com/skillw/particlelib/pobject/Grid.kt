package com.skillw.particlelib.pobject

import taboolib.common.util.Location
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * 创建网格 Grid
 *
 * @param minLoc 起点
 * @param maxLoc 终点
 * @param length 网格长度
 * @constructor
 */
class Grid constructor(
    var minLoc: Location,
    var maxLoc: Location,
    val length: Double = 1.2,
) : ParticleObject(minLoc) {
    private var isXDimension = false
    private var isYDimension = false

    init {
        // 平面检查
        if (minLoc.blockX != maxLoc.blockX) {
            if (minLoc.blockZ != maxLoc.blockZ) {
                require(minLoc.blockY == maxLoc.blockY) { "请将两点设定在X平面, Y平面或Z平面上(即一个方块的面上)" }
            }
        }
        if (minLoc.blockX == maxLoc.blockX) {
            isXDimension = false
        }
        if (minLoc.blockY == maxLoc.blockY) {
            isYDimension = true
        }
        if (minLoc.blockZ == maxLoc.blockZ) {
            isXDimension = true
        }
    }

    override fun show() {
        // 为防止给定的最小和最高点出现反向的情况, 这里做了个查找操作
        val minLocation = findMinimumLocation()
        val maxLocation = findMaximumLocation()
        val height: Double
        val width: Double

        // 在Y平面下有点不一样
        if (isYDimension) {
            height = abs(minLocation.x - maxLocation.x)
            width = abs(minLocation.z - maxLocation.z)
        } else {
            height = abs(this.maxLoc.y - this.minLoc.y)
            width = if (isXDimension) {
                abs(this.maxLoc.x - this.minLoc.x)
            } else {
                abs(this.maxLoc.z - this.minLoc.z)
            }
        }
        val heightSideLine = (height / length).toInt()
        val widthSideLine = (width / length).toInt()
        if (isYDimension) {
            for (i in 1..heightSideLine) {
                val vector = maxLocation.clone().subtract(minLocation).toVector()
                vector.setZ(0).normalize()
                val start = minLocation.clone().add(0.0, 0.0, i * length)
                var j = 0.0
                while (j < width) {
                    spawnParticle(start.clone().add(vector.clone().multiply(j)))
                    j += 0.2
                }
            }
            for (i in 1..widthSideLine) {
                val vector = maxLocation.clone().subtract(minLocation).toVector()
                vector.setX(0).normalize()
                val start = minLocation.clone().add(i * length, 0.0, 0.0)
                var j = 0.0
                while (j < height) {
                    spawnParticle(start.clone().add(vector.clone().multiply(j)))
                    j += 0.2
                }
            }
            return
        }
        for (i in 1..heightSideLine) {
            val vector = maxLocation.clone().subtract(minLocation).toVector()
            vector.setY(0).normalize()
            val start = minLocation.clone().add(0.0, i * length, 0.0)
            var j = 0.0
            while (j < width) {
                spawnParticle(start.clone().add(vector.clone().multiply(j)))
                j += 0.2
            }
        }
        for (i in 1..widthSideLine) {
            val vector = maxLocation.clone().subtract(minLocation).toVector()
            val start: Location = if (isXDimension) {
                vector.setX(0).normalize()
                minLocation.clone().add(i * length, 0.0, 0.0)
            } else {
                vector.setZ(0).normalize()
                minLocation.clone().add(0.0, 0.0, i * length)
            }
            var j = 0.0
            while (j < height) {
                spawnParticle(start.clone().add(vector.clone().multiply(j)))
                j += 0.2
            }
        }
    }

    private fun findMinimumLocation(): Location {
        val minX = min(minLoc.x, maxLoc.x)
        val minY = min(minLoc.y, maxLoc.y)
        val minZ = min(minLoc.z, maxLoc.z)
        return Location(minLoc.world, minX, minY, minZ)
    }

    private fun findMaximumLocation(): Location {
        val maxX = max(minLoc.x, maxLoc.x)
        val maxY = max(minLoc.y, maxLoc.y)
        val maxZ = max(minLoc.z, maxLoc.z)
        return Location(minLoc.world, maxX, maxY, maxZ)
    }
}