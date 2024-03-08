package com.skillw.pouvoir.api.feature.realizer.component


import com.skillw.pouvoir.api.plugin.`object`.BaseObject
import org.bukkit.entity.LivingEntity

/**
 * @className Realizable
 *
 * @author Glom
 * @date 2023/1/5 16:25 Copyright 2022 user.
 */
interface Realizable {
    val priority: Int
    fun compareTo(other: BaseObject): Int = if (this.priority == other.priority) 0
    else if (this.priority > other.priority) 1
    else -1

    fun realize(entity: LivingEntity)
    fun unrealize(entity: LivingEntity)
}