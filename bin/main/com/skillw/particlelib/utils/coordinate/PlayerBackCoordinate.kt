package com.skillw.particlelib.utils.coordinate

import taboolib.common.util.Location
import com.skillw.particlelib.utils.LocationUtils

/**
 * 表示一个玩家后背坐标系
 *
 * 将玩家背后转换为一个直角坐标系
 *
 * @author Zoyn
 */
class PlayerBackCoordinate(playerLocation: Location) : Coordinate {
    private val originDot: Location
    private val rotateAngle: Double

    init {
        // 旋转的角度
        rotateAngle = playerLocation.yaw.toDouble()
        originDot = playerLocation.clone()
        // 重设仰俯角
        originDot.pitch = 0f
        // 使原点与玩家有一点点距离
        originDot.add(originDot.direction.multiply(-0.3))
    }

    override fun newLocation(x: Double, y: Double, z: Double): Location? {
        return LocationUtils.rotateLocationAboutPoint(originDot.clone().add(-x, y, z), rotateAngle, originDot)
    }
}