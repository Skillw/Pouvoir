package com.skillw.particlelib.utils

import taboolib.common.util.Location
import taboolib.common.util.Vector
import kotlin.math.cos
import kotlin.math.sin

/**
 * 向量工具类
 *
 * @author Zoyn
 */
object VectorUtils {
    /**
     * 只通过数字本身相减得到向量, 减少额外克隆的损耗
     *
     * @param start 起点
     * @param end 终点
     * @return [Vector]
     */
    fun createVector(start: Location, end: Location): Vector {
        return Vector(end.x - start.x, end.y - start.y, end.z - start.z)
    }

    fun getLeftDirection(location: Location): Vector {
        return rotateAroundAxisY(location.direction.clone(), 90.0)
    }

    fun getRightDirection(location: Location): Vector {
        return rotateAroundAxisY(location.direction.clone(), -90.0)
    }

    /**
     * 得到一个单位为 1 的向上的向量
     *
     * @return [Vector]
     */
    fun getUpVector(): Vector {
        return getUpVector(1.0)
    }

    fun getUpVector(multiply: Double): Vector {
        return Vector(0, 1, 0).multiply(multiply)
    }

    fun rotateAroundAxisX(vec: Vector, angleValue: Double): Vector {
        var angle = angleValue
        angle = Math.toRadians(angle)
        val cos = cos(angle)
        val sin = sin(angle)
        val y = vec.y * cos - vec.z * sin
        val z = vec.y * sin + vec.z * cos
        return vec.setY(y).setZ(z)
    }

    fun rotateAroundAxisY(vec: Vector, angleValue: Double): Vector {
        var angle = angleValue
        angle = -angle
        angle = Math.toRadians(angle)
        val cos = cos(angle)
        val sin = sin(angle)
        val x = vec.x * cos + vec.z * sin
        val z = vec.x * -sin + vec.z * cos
        return vec.setX(x).setZ(z)
    }

    fun rotateAroundAxisZ(vec: Vector, angleValue: Double): Vector {
        var angle = angleValue
        angle = Math.toRadians(angle)
        val cos = cos(angle)
        val sin = sin(angle)
        val x = vec.x * cos - vec.y * sin
        val y = vec.x * sin + vec.y * cos
        return vec.setX(x).setY(y)
    }

    /**
     * This handles non-unit vectors, with yaw and pitch instead of X,Y,Z
     * angles.
     *
     * Thanks to SexyToad!
     *
     * 将一个非单位向量使用yaw和pitch来代替X, Y, Z的角旋转方式
     *
     * @param v 向量
     * @param yawDegrees yaw的角度
     * @param pitchDegrees pitch的角度
     * @return [Vector]
     */
    fun rotateVector(v: Vector, yawDegrees: Float, pitchDegrees: Float): Vector {
        val yaw = Math.toRadians((-1 * (yawDegrees + 90)).toDouble())
        val pitch = Math.toRadians(-pitchDegrees.toDouble())
        val cosYaw = cos(yaw)
        val cosPitch = cos(pitch)
        val sinYaw = sin(yaw)
        val sinPitch = sin(pitch)

        // Z_Axis rotation (Pitch)
        var initialX: Double = v.x
        val initialY: Double = v.y
        var x: Double = initialX * cosPitch - initialY * sinPitch
        val y: Double = initialX * sinPitch + initialY * cosPitch

        // Y_Axis rotation (Yaw)
        val initialZ: Double = v.z
        initialX = x
        val z: Double = initialZ * cosYaw - initialX * sinYaw
        x = initialZ * sinYaw + initialX * cosYaw
        return Vector(x, y, z)
    }
}