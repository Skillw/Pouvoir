package com.skillw.pouvoir.internal.core.asahi.infix.bukkit

import com.skillw.asahi.api.annotation.AsahiInfix
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix
import com.skillw.pouvoir.util.getEntityRayHit
import org.bukkit.entity.LivingEntity
import taboolib.library.xseries.XPotion

/**
 * @className ActionLivingEntity
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AsahiInfix
object InfixLivingEntity : BaseInfix<LivingEntity>(LivingEntity::class.java, "bukkit") {
    init {

        infix("health") { entity ->
            if (expect("to"))
                entity.health = parse()
            else
                entity.health
        }

        infix("maxHealth") { entity ->
            if (expect("to"))
                entity.maxHealth = parse()
            else
                entity.maxHealth
        }
        infix("potion") { entity ->
            when (val type = next()) {
                "add" -> {
                    val potion = parse<XPotion>()
                }
            }
        }
        infix("nearbyEntities") { entity ->
            entity.getNearbyEntities(parse(), parse(), parse())
        }
        infix("rayHitEntity") { entity ->
            entity.getEntityRayHit(parse())
        }
    }
}