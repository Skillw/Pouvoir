package com.skillw.particlelib.utils.coordinate

import taboolib.common.util.Location
import com.skillw.particlelib.utils.LocationUtils

/**
 * 表示一个将X轴显示在玩家面前的坐标器
 *
 * 自动修正在XZ平面上的粒子朝向
 *
 * @author Zoyn
 */
class PlayerFixedCoordinate(playerLocation: Location) : Coordinate {
    /** 原点 */
    private val originDot: Location

    /** 旋转角度 */
    private val rotateAngle: Double

    init {
        // 旋转的角度
        rotateAngle = playerLocation.yaw.toDouble()
        originDot = playerLocation.clone()
        // 重设仰俯角, 防止出现仰头后旋转角度不正确的问题
        originDot.pitch = 0f
    }

    fun getOriginDot(): Location {
        return originDot
    }

    override fun newLocation(x: Double, y: Double, z: Double): Location? {
        return LocationUtils.rotateLocationAboutPoint(originDot.clone().add(-x, y, z), rotateAngle, originDot)
    }
}