package com.skillw.pouvoir.internal.core.function.functions.common.bukkit

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import org.bukkit.Bukkit
import org.bukkit.World

/**
 * @className FunctionBlock
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionWorld : PouFunction<World>(
    "world"
) {
    override fun execute(parser: Parser): World {
        with(parser) {
            val name = parseString()
            return Bukkit.getWorld(name) ?: error("World not found: $name")
        }
    }
}