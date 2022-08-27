package com.skillw.pouvoir.internal.core.function.action

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.IAction
import com.skillw.pouvoir.api.function.parser.Parser
import org.bukkit.Location
import org.bukkit.entity.Entity
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
object ActionEntity : IAction {
    override val actions: Set<String>
        get() = hashSetOf(
            "location",
            "teleport",
            "name",
            "displayName",
            "velocity",
            "metadata",
        )
    override val type: Class<*>
        get() = Entity::class.java

    override fun action(parser: Parser, obj: Any, action: String): Any? {
        if (obj !is Entity) error("obj is not a Entity")
        with(parser) {
            return when (action) {
                "location" -> {
                    return obj.location
                }

                "teleport" -> {
                    except("to")
                    when (val any = parseAny()) {
                        is Entity -> obj.teleport(any)
                        is Location -> obj.teleport(any)
                        else -> error("Wrong argument type, only Location / Entity")
                    }
                }

                "name" -> {
                    return obj.name
                }

                "displayName" -> {
                    return obj.getI18nName()
                }

                "velocity" -> {
                    if (except("to"))
                        obj.velocity = parse()
                    else
                        return obj.velocity
                }

                "metadata" -> {
                    val key = parseString()
                    if (except("to"))
                        obj.setMeta(key, parseAny())
                    else
                        return obj.getMetaFirst(key)
                }

                else -> null
            }
        }
    }
}