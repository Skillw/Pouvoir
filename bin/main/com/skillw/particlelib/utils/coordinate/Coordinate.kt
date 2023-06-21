package com.skillw.particlelib.utils.coordinate

import taboolib.common.util.Location

/**
 * 表示一个坐标器
 *
 * @author Zoyn
 */
interface Coordinate {
    fun newLocation(x: Double, y: Double, z: Double): Location?
}