package com.skillw.pouvoir.util

import com.skillw.pouvoir.Pouvoir.sync
import com.skillw.pouvoir.internal.feature.raytrace.RayTrace
import com.skillw.pouvoir.util.plugin.Pair
import com.skillw.pouvoir.util.plugin.to
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import taboolib.common.platform.function.isPrimaryThread
import taboolib.common.util.sync
import taboolib.module.navigation.BoundingBox
import taboolib.module.navigation.NMSImpl
import taboolib.module.nms.getI18nName
import taboolib.platform.util.toBukkitLocation
import java.util.*

/**
 * 实体工具类
 *
 * ClassName : com.skillw.pouvoir.feature.EntityUtil Created by Glom_ on
 * 2021-03-28 17:49:01 Copyright 2021 user. All rights reserved.
 */

fun LivingEntity.getDisplayName(): String {
    return getName(this).toString()
}


fun getName(entity: LivingEntity): String? {
    return if (entity is Player) entity.displayName else entity.getI18nName()
}

fun UUID.livingEntity(): LivingEntity? {
    return getLivingEntityByUUID(this)
}


fun getLivingEntityByUUID(uuid: UUID?): LivingEntity? {
    val entity = getEntity(uuid)
    return entity?.run {
        if (isLiving(this)) {
            entity as LivingEntity?
        } else null
    }
}

fun UUID.player(): Player? {
    return getPlayerByUUID(this)
}


fun getPlayerByUUID(uuid: UUID?): Player? {
    return if (sync && !isPrimaryThread) sync { Bukkit.getPlayer(uuid!!) } else Bukkit.getPlayer(uuid!!)
}

fun UUID.isAlive(): Boolean {
    return isLiving(this)
}


fun getEntity(uuid: UUID?): Entity? {
    return uuid?.let { if (sync && !isPrimaryThread) sync { Bukkit.getEntity(it) } else Bukkit.getEntity(it) }
}


fun isLiving(uuid: UUID?): Boolean {
    val entity = getEntity(uuid)
    return isLiving(entity)
}

fun Entity.isAlive(): Boolean {
    return isLiving(this)
}


fun isLiving(entity: Entity?): Boolean {
    return entity is LivingEntity && !entity.isDead() && entity.isValid
}


fun LivingEntity.getEntityRayHit(
    distance: Double,
): Entity? {
    return getEntityRayHits(distance).firstOrNull()
}


fun Location.getNearByEntities(x: Double, y: Double, z: Double): Collection<Entity> {
    return if (sync && !isPrimaryThread)
        sync { world?.getNearbyEntities(this, x, y, z) ?: emptyList() }
    else world?.getNearbyEntities(this, x, y, z) ?: emptyList()
}


fun taboolib.common.util.Location.getNearByEntities(x: Double, y: Double, z: Double): Collection<Entity> {
    return toBukkitLocation().getNearByEntities(x, y, z)
}


fun taboolib.common.util.Location.getEntities(): Collection<Entity> {
    return toBukkitLocation().getNearByEntities(0.5, 0.5, 0.5)
}


fun Location.getRayHit(distance: Double): Entity? {
    return getRayHits(distance).firstOrNull()
}


fun Location.getRayHits(distance: Double): Collection<Entity> {
    val entities = ArrayList<Pair<Entity, BoundingBox>>()
    getNearByEntities(distance, distance, distance)
        .forEach {
            entities += it to NMSImpl().getBoundingBox(it);
        }
    val traces = RayTrace(this.toVector(), this.direction).traces(distance, 0.2)
    val result = LinkedList<Entity>()
    for (vector in traces) {
        result.addAll(entities.filter { it.value.contains(vector) }.map { it.key })
    }
    return result
}


fun LivingEntity.getEntityRayHits(
    distance: Double,
): Collection<Entity> {
    return eyeLocation.getRayHits(distance).filter { it != this }
}