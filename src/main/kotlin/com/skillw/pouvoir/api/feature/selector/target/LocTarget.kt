package com.skillw.pouvoir.api.feature.selector.target

import com.skillw.pouvoir.api.feature.selector.Target
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

/**
 * @className EntityTarget
 *
 * @author Glom
 * @date 2023/1/9 7:36 Copyright 2023 user. All rights reserved.
 */
open class LocTarget(location: Location) : Target("LocTarget$$location", location) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LocTarget) return false

        if (location != other.location) return false
        return true
    }

    override fun hashCode(): Int {
        return location.hashCode()
    }

    override fun toString(): String {
        return "LocTarget(loc=$location)"
    }

    companion object {
        fun Entity.targetLoc(): Location {
            return if (this is LivingEntity) eyeLocation.clone() else location.clone()
        }
    }

}