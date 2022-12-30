package com.skillw.pouvoir.internal.core.function.action

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.PouAction
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.module.nms.getI18nName
import taboolib.platform.util.getMetaFirst
import taboolib.platform.util.setMeta

/**
 * @className ActionPlayer
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object ActionEntity : PouAction<Entity>(Entity::class.java) {
    init {
        addExec("location") {
            it.location
        }
        addExec("teleport") { obj ->
            except("to")
            when (val any = parseAny()) {
                is Entity -> obj.teleport(any)
                is Location -> obj.teleport(any)
                else -> error("Wrong argument type, only Location / Entity")
            }
        }

        addExec("name") { obj ->
            obj.name
        }

        addExec("displayName") { obj ->
            obj.getI18nName()
        }

        addExec("velocity") { obj ->
            if (except("to"))
                obj.velocity = parse()
            else
                obj.velocity
        }

        addExec("metadata") { obj ->
            val key = parseString()
            if (except("to"))
                obj.setMeta(key, parseAny())
            else
                obj.getMetaFirst(key)
        }
        addExec("isPlayer") { obj ->
            obj is Player
        }
    }
}