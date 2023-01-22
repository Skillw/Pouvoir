package com.skillw.pouvoir.api.feature.selector

import com.skillw.pouvoir.api.feature.selector.target.EntityTarget
import com.skillw.pouvoir.api.feature.selector.target.LocTarget
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

/**
 * @className SelectResult
 *
 * 选取结果
 *
 * @author Glom
 * @date 2023/1/9 7:20 Copyright 2023 user. All rights reserved.
 */
class SelectResult(targets: Collection<Target> = LinkedHashSet()) : LinkedHashSet<Target>(targets) {
    /**
     * 遍历
     *
     * @param consumer
     * @param T
     * @receiver
     */
    inline fun <reified T> forEach(consumer: T.(Int) -> Unit) =
        filterIsInstance<T>().forEachIndexed { index, entity -> entity.run { consumer(index) } }

    /**
     * 遍历 entity
     *
     * @param consumer
     * @receiver
     */
    fun forEachEntity(consumer: Entity.(Int) -> Unit) =
        forEach<EntityTarget> { index -> if (isPresent) consumer(entity, index) }

    /**
     * 遍历 living entity
     *
     * @param consumer
     * @receiver
     */
    fun forEachLivingEntity(consumer: LivingEntity.(Int) -> Unit) =
        forEach<EntityTarget> { index -> if (isPresent) castSafely<LivingEntity>()?.also { consumer(it, index) } }

    /**
     * 遍历 location
     *
     * @param consumer
     * @receiver
     */
    fun forEachLocation(consumer: Location.(Int) -> Unit) =
        forEach<LocTarget> { index -> if (isPresent) consumer(location, index) }
}