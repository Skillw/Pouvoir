package com.skillw.pouvoir.api.feature.selector.target

import org.bukkit.entity.Entity

/**
 * @className EntityTarget
 *
 * @author Glom
 * @date 2023/1/9 7:36 Copyright 2024 Glom.
 */
@Suppress("UNCHECKED_CAST")
class EntityTarget(val entity: Entity) : LocTarget(entity.targetLoc()) {
    override val unique: String = entity.uniqueId.toString()
    override val isPresent: Boolean
        get() = !entity.isDead && entity.isValid

    /**
     * 强制转换
     *
     * @param T 类型
     * @return 转换后的Target
     */
    fun <T : Entity> cast(): T = entity as T

    /**
     * 安全转换
     *
     * @param T 类型
     * @return 转换后的Target
     */
    fun <T : Entity> castSafely(): T? = entity as? T

    override fun toString(): String {
        return "EntityTarget( entity=$entity loc=$location)"
    }
}