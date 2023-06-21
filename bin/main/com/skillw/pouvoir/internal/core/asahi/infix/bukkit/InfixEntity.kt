package com.skillw.pouvoir.internal.core.asahi.infix.bukkit

import com.skillw.asahi.api.annotation.AsahiInfix
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix
import org.bukkit.Location
import org.bukkit.entity.Entity
import taboolib.module.nms.getI18nName
import taboolib.platform.util.getMetaFirst
import taboolib.platform.util.setMeta

/**
 * @className ActionEntity
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AsahiInfix
object InfixEntity : BaseInfix<Entity>(Entity::class.java, "bukkit") {
    init {
        infix("location") {
            it.location
        }
        infix("teleport") { obj ->
            expect("to")
            when (val any = parse<Any>()) {
                is Entity -> obj.teleport(any)
                is Location -> obj.teleport(any)
                else -> error("Wrong argument type, only Location / Entity")
            }
        }

        infix("name") { obj ->
            obj.name
        }

        infix("displayName") { obj ->
            obj.getI18nName()
        }

        infix("velocity") { obj ->
            if (expect("to"))
                obj.velocity = parse()
            else
                obj.velocity
        }

        infix("metadata") { obj ->
            val key = parse<String>()
            if (expect("to"))
                obj.setMeta(key, parse())
            else
                obj.getMetaFirst(key)
        }

        infix("fireTicks") { obj ->
            if (expect("to"))
                obj.fireTicks = parse()
            else
                obj.fireTicks
        }

        infix("fallDistance") { obj ->
            if (expect("to"))
                obj.fallDistance = parse()
            else
                obj.fallDistance
        }
    }
}