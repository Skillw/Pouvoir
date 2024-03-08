package com.skillw.pouvoir.internal.feature.selector

import org.bukkit.Location
import taboolib.module.navigation.BoundingBox
import kotlin.math.acos


/**
 * @className Util
 *
 * @author Glom
 * @date 2023/1/10 16:36 Copyright 2024 Glom.
 */

fun BoundingBox(start: Location, end: Location): BoundingBox {
    return BoundingBox(start.x, start.y, start.z, end.x, end.y, end.z)
}

/**
 * 判断一个Loc是否处在实体面向的扇形区域内
 *
 * 通过反三角算向量夹角的算法
 *
 * 来自
 * https://github.com/602723113/ParticleLib/blob/master/src/main/java/top/zoyn/particlelib/utils/LocationUtils.java
 *
 * @param start 原点
 * @param target 目标坐标
 * @param radius 扇形半径
 * @param angle 扇形角度
 * @return 如果处于扇形区域则返回 true
 */
fun isPointInEntitySector(start: Location, target: Location, radius: Double, angle: Double): Boolean {
    val v1 = start.direction
    val v2 = target.clone().subtract(start).toVector()
    val cosTheta: Double = v1.dot(v2) / (v1.length() * v2.length())
    val degree = Math.toDegrees(acos(cosTheta))
    return if (target.distance(start) < radius) degree < angle * 0.5f else false
}

/**
 * Checks whether or not the line between the two points is obstructed
 *
 * @param loc1 first location
 * @param loc2 second location
 * @return the location of obstruction or null if not obstructed
 */
fun isObstructed(loc1: Location, loc2: Location): Boolean {
    if (loc1.x == loc2.x && loc1.y == loc2.y && loc1.z == loc2.z) {
        return false
    }
    val slope = loc2.clone().subtract(loc1).toVector()
    val steps = (slope.length() * 4).toInt() + 1
    slope.multiply(1.0 / steps)
    val temp = loc1.clone()
    for (i in 0 until steps) {
        temp.add(slope)
        if (temp.block.type.isSolid) {
            return true
        }
    }
    return false
}