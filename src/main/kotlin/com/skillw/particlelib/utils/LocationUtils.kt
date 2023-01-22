package com.skillw.particlelib.utils

import org.bukkit.entity.LivingEntity
import taboolib.common.util.Location
import taboolib.common.util.Vector
import taboolib.platform.util.toProxyLocation
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

/**
 * 坐标工具类
 *
 * @author Zoyn
 */
object LocationUtils {
    /**
     * 在二维平面上利用给定的中心点逆时针旋转一个点
     *
     * @param location 待旋转的点
     * @param angle 旋转角度
     * @param point 中心点
     * @return [Location]
     */
    fun rotateLocationAboutPoint(location: Location?, angle: Double, point: Location?): Location {
        val radians = Math.toRadians(angle)
        val dx = location!!.x - point!!.x
        val dz = location.z - point.z
        val newX = dx * cos(radians) - dz * sin(radians) + point.x
        val newZ = dz * cos(radians) + dx * sin(radians) + point.z
        return Location(location.world, newX, location.y, newZ)
    }

    fun rotateLocationAboutVector(location: Location, origin: Location, angle: Double, axis: Vector): Location {
        val vector = location.clone().subtract(origin).toVector()
        return origin.clone().add(VectorUtils.rotateAroundAxisY(axis, angle))
    }

    /**
     * 判断一个是否处在实体面向的扇形区域内
     *
     * 通过反三角算向量夹角的算法
     *
     * @param target 目标坐标
     * @param entity 实体
     * @param radius 扇形半径
     * @param angle 扇形角度
     * @return 如果处于扇形区域则返回 true
     */
    fun isPointInEntitySector(target: Location, entity: LivingEntity, radius: Double, angle: Double): Boolean {
        val entityLoc = entity.location.toProxyLocation()
        val v1 = entityLoc.direction
        val v2 = target.clone().subtract(entityLoc).toVector()
        val cosTheta = v1.dot(v2) / (v1.length() * v2.length())
        val degree = Math.toDegrees(acos(cosTheta))
        // 距离判断
        return if (target.distance(entityLoc) < radius) {
            // 向量夹角判断
            degree < angle * 0.5f
        } else false
    }

    /**
     * 判断一个是否处在实体面向的扇形区域内
     *
     * 通过叉乘算法
     *
     * @param target 目标坐标
     * @param entity 实体
     * @param radius 扇形半径
     * @param angle 扇形角度
     * @return 如果处于扇形区域则返回 true
     */
    fun isInsideSector(target: Location, entity: LivingEntity, radius: Double, angle: Double): Boolean {
        val entityLoc = entity.location.toProxyLocation()
        val entityDir = entityLoc.direction
        val sectorStart = VectorUtils.rotateAroundAxisY(entityDir.clone(), -angle / 2)
        val sectorEnd = VectorUtils.rotateAroundAxisY(entityDir.clone(), angle / 2)
        val v = target.clone().subtract(entity.location.toProxyLocation()).toVector()
        val start = -sectorStart.x * v.z + sectorStart.z * v.x > 0
        val end = -sectorEnd.x * v.z + sectorEnd.z * v.x > 0
        return !start && end && target.distance(entityLoc) < radius
    }
}