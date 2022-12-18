package com.skillw.pouvoir.internal.core.function.action

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.PouAction
import com.skillw.pouvoir.util.EntityUtils.getDisplayName
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import taboolib.platform.util.getMetaFirst
import taboolib.platform.util.setMeta

/**
 * @className ActionPlayer
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object ActionLivingEntity : PouAction<LivingEntity>(LivingEntity::class.java) {
    init {
        addExec("location") { entity ->
            entity.location
        }

        addExec("teleport") { entity ->
            except("to")
            when (val any = parseAny()) {
                is Entity -> entity.teleport(any)
                is Location -> entity.teleport(any)
                else -> error("Wrong argument type, only Location / Entity")
            }
            
        }

        addExec("name") { entity ->
            entity.name
        }

        addExec("displayName") { entity ->
            entity.getDisplayName()
        }

        addExec("health") { entity ->
            if (except("to"))
                entity.health = parseDouble()
            else
                entity.health
        }

        addExec("maxHealth") { entity ->
            if (except("to"))
                entity.maxHealth = parseDouble()
            else
                entity.maxHealth
        }

        addExec("velocity") { entity ->
            if (except("to"))
                entity.velocity = parse()
            else
                entity.velocity
        }

        addExec("metadata") { entity ->
            val key = parseString()
            if (except("to"))
                entity.setMeta(key, parseAny())
            else
                entity.getMetaFirst(key)
        }
        addExec("potion") { entity ->

        }
    }
}