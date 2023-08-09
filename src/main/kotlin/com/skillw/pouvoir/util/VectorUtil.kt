package com.skillw.pouvoir.util

import org.bukkit.util.Vector
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


/**
 * @className VectorUtils
 *
 * @author Glom
 * @date 2023/1/17 22:01 Copyright 2023 user. All rights reserved.
 */
fun Vector.rotateAroundXp(angle: Double): Vector {
    val angleCos = cos(angle)
    val angleSin = sin(angle)
    val y: Double = angleCos * this.y - angleSin * this.z
    val z: Double = angleSin * this.y + angleCos * this.z
    return this.setY(y).setZ(z)
}


fun Vector.rotateAroundYp(angle: Double): Vector {
    val angleCos = cos(angle)
    val angleSin = sin(angle)
    val x: Double = angleCos * this.x + angleSin * this.z
    val z: Double = -angleSin * this.x + angleCos * this.z
    return this.setX(x).setZ(z)
}


fun Vector.rotateAroundZp(angle: Double): Vector {
    val angleCos = cos(angle)
    val angleSin = sin(angle)
    val x: Double = angleCos * this.x - angleSin * this.y
    val y: Double = angleSin * this.x + angleCos * this.y
    return this.setX(x).setY(y)
}


fun Vector.angleFixed(other: Vector): Float {
    val dot = dot(other) / length() * other.length()
    return acos(dot).toFloat()
}


fun org.bukkit.util.Vector.toProxyVector(): taboolib.common.util.Vector {
    return taboolib.common.util.Vector(x, y, z)
}