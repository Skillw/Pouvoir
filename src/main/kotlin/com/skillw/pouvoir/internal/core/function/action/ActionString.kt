package com.skillw.pouvoir.internal.core.function.action

import com.skillw.pouvoir.api.PouvoirAPI.placeholder
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.IAction
import com.skillw.pouvoir.api.function.parser.Parser
import org.bukkit.entity.LivingEntity

/**
 * @className ActionMap
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object ActionString : IAction {
    override val actions: Set<String> =
        hashSetOf("placeholder")
    override val type: Class<*> = String::class.java

    override fun action(parser: Parser, obj: Any, action: String): Any {
        obj as? String? ?: error("obj is not a String")
        with(parser) {
            when (action) {
                "placeholder" -> {
                    val entity = parse<LivingEntity>()
                    return obj.placeholder(entity)
                }

                else -> {
                    error("unknown pair $obj action $action")
                }
            }
        }
    }

}