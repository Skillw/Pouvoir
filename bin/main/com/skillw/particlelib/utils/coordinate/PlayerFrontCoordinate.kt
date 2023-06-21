package com.skillw.particlelib.utils.coordinate

import taboolib.common.util.Location
import com.skillw.particlelib.utils.LocationUtils

/**
 * 表示一个玩家面前的坐标系
 *
 * 将玩家面前作为一个新坐标系 暂时不会受到仰俯角的控制
 *
 * @author Zoyn
 */
class PlayerFrontCoordinate(playerLocation: Location) : Coordinate {
    /** 原点 */
    private val originDot: Location

    /** 旋转角度 */
    private val rotateAngle: Double

    init {
        // 旋转的角度
        rotateAngle = playerLocation.yaw + 90.0
        originDot = playerLocation.clone()
        // 重设仰俯角, 防止出现仰头后旋转角度不正确的问题
        originDot.pitch = 0f
    }

    fun getOriginDot(): Location {
        return originDot
    }

    override fun newLocation(x: Double, y: Double, z: Double): Location? {
        return LocationUtils.rotateLocationAboutPoint(originDot.clone().add(y, z, x), rotateAngle, originDot)
    }
}