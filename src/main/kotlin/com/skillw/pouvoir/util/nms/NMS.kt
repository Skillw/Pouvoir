package com.skillw.pouvoir.util.nms

import com.skillw.pouvoir.util.attribute.BukkitAttribute
import org.bukkit.Location
import org.bukkit.attribute.AttributeInstance
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import taboolib.module.nms.nmsProxy
import java.util.*
import java.util.function.Predicate

/**
 * @className NMS
 *
 * @author Glom
 * @date 2022/8/9 22:24 Copyright 2022 user.
 */
abstract class NMS {

    abstract fun getAttribute(entity: LivingEntity, attribute: BukkitAttribute): AttributeInstance?

    companion object {

        val INSTANCE by lazy {
            nmsProxy<NMS>()
        }
    }

    abstract fun getEntity(uuid: UUID): Entity?
    abstract fun getNearbyEntities(
        location: Location,
        x: Double,
        y: Double,
        z: Double,
        filter: Predicate<Entity>,
    ): Collection<Entity>
}