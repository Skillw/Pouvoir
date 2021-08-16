package com.skillw.pouvoir.util

import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import java.util.*

/**
 * ClassName : com.skillw.com.skillw.rpglib.util.EntityUtils
 * Created by Glom_ on 2021-03-28 17:49:01
 * Copyright  2021 user. All rights reserved.
 */
object EntityUtils {
    @JvmStatic
    fun getName(livingEntity: LivingEntity?): String? {
        if (livingEntity == null) {
            return null
        }
        val name = livingEntity.name
        return if (name.contains("§")) name else "&6$name"
    }

    @JvmStatic
    fun getLivingEntityByUUID(uuid: UUID?): LivingEntity? {
        val entity = Bukkit.getEntity(uuid!!)
        return if (entity != null && isLiving(entity)) {
            entity as LivingEntity?
        } else null
    }

    @JvmStatic
    fun getPlayerByUUID(uuid: UUID?): Player? {
        return Bukkit.getPlayer(uuid!!)
    }

    @JvmStatic
    fun isLiving(uuid: UUID?): Boolean {
        val entity = Bukkit.getEntity(uuid!!)
        return isLiving(entity)
    }

    @JvmStatic
    fun isLiving(entity: Entity?): Boolean {
        return entity is LivingEntity && entity.getType() != EntityType.ARMOR_STAND && !entity.isDead()
    }
}