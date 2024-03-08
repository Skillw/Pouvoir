package com.skillw.pouvoir.util

import com.skillw.pouvoir.Pouvoir.sync
import com.skillw.pouvoir.internal.feature.raytrace.RayTrace
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import taboolib.common.platform.function.isPrimaryThread
import taboolib.module.navigation.NMS
import taboolib.module.nms.getI18nName
import taboolib.platform.util.toBukkitLocation
import java.util.*
import java.util.function.Predicate

/**
 * 实体工具类
 *
 * ClassName : com.skillw.pouvoir.feature.EntityUtils Created by Glom_ on
 * 2021-03-28 17:49:01 Copyright 2021 user. 
 */


fun LivingEntity.getDisplayName(): String {
    return getName(this).toString()
}


fun getName(entity: LivingEntity): String? {
    return if (entity is Player) entity.displayName else entity.customName ?: entity.getI18nName()
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


fun UUID.isAlive(): Boolean {
    return isLiving(this)
}

// 就下面这俩b函数 1.19以上不同步会直接刷爆后台
fun getPlayerByUUID(uuid: UUID?): Player? {
    return Bukkit.getPlayer(uuid!!)
}

fun getEntity(uuid: UUID?): Entity? {
    return uuid?.let {
        if (sync && !isPrimaryThread) com.skillw.pouvoir.util.nms.NMS.INSTANCE.getEntity(uuid) else Bukkit.getEntity(
            it
        )
    }
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


fun Location.getNearByEntities(
    x: Double, y: Double, z: Double,
    filter: Predicate<Entity> = Predicate { true },
): Collection<Entity> {
    return if (sync) com.skillw.pouvoir.util.nms.NMS.INSTANCE.getNearbyEntities(
        this,
        x,
        y,
        z,
        filter
    ) else world?.getNearbyEntities(this, x, y, z)?.filter { filter.test(it) } ?: emptyList()
}


fun taboolib.common.util.Location.getNearByEntities(
    x: Double, y: Double, z: Double,
    filter: Predicate<Entity> = Predicate { true },
): Collection<Entity> {
    return toBukkitLocation().getNearByEntities(x, y, z, filter)
}


fun taboolib.common.util.Location.getEntities(filter: Predicate<Entity> = Predicate { true }): Collection<Entity> {
    return toBukkitLocation().getNearByEntities(0.5, 0.5, 0.5, filter)
}


fun Location.getRayHit(distance: Double): Entity? {
    return getRayHits(distance).firstOrNull()
}


fun Location.getRayHits(distance: Double): Collection<Entity> {
    val traces = RayTrace(this.toVector(), this.direction).traces(distance, 0.2)
    return getNearByEntities(distance, distance, distance) { entity ->
        traces.any { NMS.instance.getBoundingBox(entity)?.contains(it) == true }
    }
}


fun LivingEntity.getEntityRayHits(
    distance: Double,
): Collection<Entity> {
    return eyeLocation.getRayHits(distance).filter { it != this }
}
