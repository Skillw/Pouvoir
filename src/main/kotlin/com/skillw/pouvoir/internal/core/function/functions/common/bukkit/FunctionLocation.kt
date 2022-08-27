package com.skillw.pouvoir.internal.core.function.functions.common.bukkit

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import org.bukkit.Location
import org.bukkit.World

/**
 * @className FunctionBlock
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionLocation : PouFunction<Location>(
    "location"
) {
    override fun execute(parser: Parser): Location {
        with(parser) {
            except("[")
            val world = parse<World>()
            val x = parse<Double>()
            val y = parse<Double>()
            val z = parse<Double>()
            except("]")
            var yaw = 0.0f
            var pitch = 0.0f
            if (except("with")) {
                yaw = parse()
                pitch = parse()
            }
            return Location(world, x, y, z, yaw, pitch)
        }
    }
}