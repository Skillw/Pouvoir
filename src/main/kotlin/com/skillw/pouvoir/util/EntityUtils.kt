package com.skillw.pouvoir.util

import com.google.common.base.Enums
import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.internal.raytrace.RayTrace
import com.skillw.pouvoir.util.PlayerUtils.sendPacketWithFields
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import taboolib.common.reflect.Reflex.Companion.getProperty
import taboolib.common.reflect.Reflex.Companion.invokeConstructor
import taboolib.common.reflect.Reflex.Companion.invokeMethod
import taboolib.common.reflect.Reflex.Companion.unsafeInstance
import taboolib.module.navigation.BoundingBox
import taboolib.module.navigation.NMSImpl
import taboolib.module.nms.MinecraftVersion.isUniversal
import taboolib.module.nms.MinecraftVersion.major
import taboolib.module.nms.MinecraftVersion.majorLegacy
import taboolib.module.nms.nmsClass
import java.util.*

/**
 * ClassName : com.skillw.pouvoir.util.EntityUtils
 * Created by Glom_ on 2021-03-28 17:49:01
 * Copyright  2021 user. All rights reserved.
 */
object EntityUtils {
    fun LivingEntity.getDisplayName(): String {
        return getName(this).toString()
    }

    @JvmStatic
    fun getName(livingEntity: LivingEntity?): String? {
        if (livingEntity == null) {
            return null
        }
        val name = livingEntity.name
        return if (name.contains("§")) name else "&6$name"
    }

    fun UUID.livingEntity(): LivingEntity? {
        return getLivingEntityByUUID(this)
    }

    @JvmStatic
    fun getLivingEntityByUUID(uuid: UUID?): LivingEntity? {
        val entity = Bukkit.getEntity(uuid!!)
        return if (entity != null && isLiving(entity)) {
            entity as LivingEntity?
        } else null
    }

    fun UUID.player(): Player? {
        return getPlayerByUUID(this)
    }

    @JvmStatic
    fun getPlayerByUUID(uuid: UUID?): Player? {
        return Bukkit.getPlayer(uuid!!)
    }

    fun UUID.isAlive(): Boolean {
        return isLiving(this)
    }

    @JvmStatic
    fun isLiving(uuid: UUID?): Boolean {
        val entity = Bukkit.getEntity(uuid!!)
       
        return isLiving(entity)
    }

    fun Entity.isAlive(): Boolean {
        return isLiving(this)
    }

    @JvmStatic
    fun isLiving(entity: Entity?): Boolean {
        return entity is LivingEntity && entity.getType() != EntityType.ARMOR_STAND && !entity.isDead()
    }

    private val ARMOR_STAND_NEW = Enums.getIfPresent(EntityType::class.java, "ARMOR_STAND").orNull()
    private const val ARMOR_STAND_LEGACY = 78
    private val ARMOR_STAND_NMS: Any by lazy {
        if (major >= 5) {
            nmsClass("EntityTypes").getProperty<Any>(
                "ARMOR_STAND", fixed = true
            )!!
        } else {
            ARMOR_STAND_LEGACY
        }
    }

    @JvmStatic
    fun spawnArmorStand(player: Player, entityId: Int, uuid: UUID, location: Location) {
        if (isUniversal) {
            player.sendPacketWithFields(
                nmsClass("PacketPlayOutSpawnEntity").unsafeInstance(),
                "id" to entityId,
                "uuid" to uuid,
                "x" to location.x,
                "y" to location.y,
                "z" to location.z,
                "xa" to 0,
                "ya" to 0,
                "za" to 0,
                "xRot" to (location.yaw * 256.0f / 360.0f).toInt().toByte(),
                "yRot" to (location.pitch * 256.0f / 360.0f).toInt().toByte(),
                "type" to ARMOR_STAND_NEW,
                "data" to 0
            )
        } else {
            player.sendPacketWithFields(
                nmsClass("PacketPlayOutSpawnEntity").unsafeInstance(),
                "a" to entityId,
                "b" to uuid,
                "c" to location.x,
                "d" to location.y,
                "e" to location.z,
                "f" to (location.yaw * 256.0f / 360.0f).toInt().toByte(),
                "g" to (location.pitch * 256.0f / 360.0f).toInt().toByte(),
                "k" to if (majorLegacy <= 11300) ARMOR_STAND_LEGACY else ARMOR_STAND_NMS
            )
        }
    }

    @JvmStatic
    fun spawnEntityLiving(
        player: Player,
        entityId: Int,
        uuid: UUID,
        location: Location
    ) {
        if (majorLegacy < 11300) {
            return spawnArmorStand(player, entityId, uuid, location)
        }
        if (major == 9) {
            player.sendPacketWithFields(
                nmsClass("PacketPlayOutSpawnEntityLiving").unsafeInstance(),
                "id" to entityId,
                "uuid" to uuid,
                "type" to nmsClass("IRegistry").getField("Y").get(null)!!
                    .invokeMethod<Int>("getId", ARMOR_STAND_NMS)!!,
                "x" to location.x,
                "y" to location.y,
                "z" to location.z,
                "xd" to 0,
                "yd" to 0,
                "zd" to 0,
                "yRot" to (location.yaw * 256.0f / 360.0f).toInt().toByte(),
                "xRot" to (location.pitch * 256.0f / 360.0f).toInt().toByte(),
                "yHeadRot" to (location.yaw * 256.0f / 360.0f).toInt().toByte()
            )
        } else {
            player.sendPacketWithFields(
                nmsClass("PacketPlayOutSpawnEntityLiving").unsafeInstance(),
                "a" to entityId,
                "b" to uuid,
                "c" to when {
                    majorLegacy >= 11800 -> nmsClass("IRegistry").getField("W").get(null)!!
                        .invokeMethod<Unit>("a", ARMOR_STAND_NMS)
                    majorLegacy >= 11400 -> nmsClass("IRegistry").getField("ENTITY_TYPE").get(null)!!
                        .invokeMethod<Unit>("a", ARMOR_STAND_NMS)
                    majorLegacy == 11300 -> nmsClass("IRegistry").getField("ENTITY_TYPE").get(null)!!
                        .invokeMethod<Unit>("a", ARMOR_STAND_NMS)
                    else -> ARMOR_STAND_LEGACY
                },
                "d" to location.x,
                "e" to location.y,
                "f" to location.z,
                "g" to 0,
                "h" to 0,
                "i" to 0,
                "j" to (location.yaw * 256.0f / 360.0f).toInt().toByte(),
                "k" to (location.pitch * 256.0f / 360.0f).toInt().toByte(),
                "l" to (location.yaw * 256.0f / 360.0f).toInt().toByte(),
                "m" to if (majorLegacy >= 11500) null else nmsClass("DataWatcher").invokeConstructor(null)
            )
        }

    }

    @JvmStatic
    fun destroyEntity(player: Player, entityId: Int) {
        player.sendPacketWithFields(nmsClass("PacketPlayOutEntityDestroy").invokeConstructor(intArrayOf(entityId)))
    }

    @JvmStatic
    fun teleportEntity(player: Player, entityId: Int, location: Location) {
        if (isUniversal) {
            player.sendPacketWithFields(
                nmsClass("PacketPlayOutEntityTeleport").unsafeInstance(),
                "id" to entityId,
                "x" to location.x,
                "y" to location.y,
                "z" to location.z,
                "yRot" to (location.yaw * 256 / 360).toInt().toByte(),
                "xRot" to (location.pitch * 256 / 360).toInt().toByte(),
                "onGround" to false
            )
        } else {
            player.sendPacketWithFields(
                nmsClass("PacketPlayOutEntityTeleport").unsafeInstance(),
                "a" to entityId,
                "b" to location.x,
                "c" to location.y,
                "d" to location.z,
                "e" to (location.yaw * 256 / 360).toInt().toByte(),
                "f" to (location.pitch * 256 / 360).toInt().toByte(),
                "g" to false // onGround
            )
        }
    }

    @JvmStatic
    fun LivingEntity.getEntityLookAt(
        distance: Double
    ): LivingEntity? {
        return getEntityRayHit(this, distance)
    }

    @JvmStatic
    fun getEntityRayHit(
        livingEntity: LivingEntity, distance: Double
    ): LivingEntity? {
        return Pouvoir.poolExecutor.submit<LivingEntity?> {
            val entities = ArrayList<Pair<Entity, BoundingBox>>()
            livingEntity.getNearbyEntities(distance, distance, distance).forEach {
                entities += it to NMSImpl().getBoundingBox(it)
            }
            val traces = RayTrace(livingEntity).traces(distance, 0.2)
            for (vector in traces) {
                val firstOrNull = entities.firstOrNull { it.value.contains(vector) }
                if (firstOrNull != null) {
                    return@submit firstOrNull.key as? LivingEntity? ?: continue
                }
            }
            return@submit null
        }.get()
    }

}