package com.skillw.pouvoir.internal.function.functions.common.bukkit

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser
import com.skillw.pouvoir.api.function.parse.Parser.Companion.parse
import org.bukkit.entity.LivingEntity

/**
 * @className FunctionBlock
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionPermission : PouFunction<Boolean>(
    "permission"
) {
    override fun execute(parser: Parser): Boolean {
        with(parser) {
            val entity = parse<LivingEntity>()
            val permissions = parseArray().map { it.toString() }
            return permissions.all { entity.hasPermission(it) }
        }
    }
}