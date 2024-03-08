package com.skillw.pouvoir.api.feature.selector

import org.bukkit.Location

/**
 * @className Target
 *
 * 目标抽象类
 *
 * @author Glom
 * @date 2023/1/9 7:32 Copyright 2024 Glom.
 */
abstract class Target(open val unique: String, val location: Location) {
    /** 是否有效 */
    open val isPresent: Boolean
        get() = true

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Target) return false

        if (unique != other.unique) return false
        if (location != other.location) return false

        return true
    }

    override fun hashCode(): Int {
        var result = unique.hashCode()
        result = 31 * result + location.hashCode()
        return result
    }
}