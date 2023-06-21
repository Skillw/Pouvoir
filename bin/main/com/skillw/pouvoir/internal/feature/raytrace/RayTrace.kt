package com.skillw.pouvoir.internal.feature.raytrace

import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

class RayTrace(private val origin: Vector, private val direction: Vector) {

    constructor(entity: LivingEntity) : this(
        entity.eyeLocation.toVector(),
        entity.eyeLocation.direction
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

    private fun distance(distance: Double): Vector {
        return origin.clone().add(direction.clone().multiply(distance))
    }
}