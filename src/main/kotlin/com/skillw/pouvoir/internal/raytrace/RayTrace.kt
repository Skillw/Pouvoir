package com.skillw.pouvoir.internal.raytrace

import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

class RayTrace(val origin: Vector, val direction: Vector) {

    constructor(livingEntity: LivingEntity) : this(
        livingEntity.eyeLocation.toVector(),
        livingEntity.eyeLocation.direction
    )

    fun traces(distance: Double, accuracy: Double): Set<Vector> {
        return mutableSetOf<Vector>().let {
            var process = 0.0
            while (process <= distance) {
                it.add(distance(process))
                process += accuracy
            }
            it
        }
    }

    fun distance(distance: Double): Vector {
        return origin.clone().add(direction.clone().multiply(distance))
    }
}