package com.skillw.pouvoir.internal.core.function.functions.common.bukkit

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import org.bukkit.util.Vector

/**
 * @className FunctionBlock
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionVector : PouFunction<Vector>(
    "vector"
) {
    override fun execute(parser: Parser): Vector {
        with(parser) {
            except("[")
            val x = parse<Double>()
            val y = parse<Double>()
            val z = parse<Double>()
            except("]")
            return Vector(x, y, z)
        }
    }
}