package com.skillw.pouvoir.internal.core.function.functions.common.bukkit

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import java.awt.Color

/**
 * @className FunctionBlock
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionColor : PouFunction<Color>(
    "color"
) {
    override fun execute(parser: Parser): Color {
        with(parser) {
            except("[")
            val r = parseInt()
            val g = parseInt()
            val b = parseInt()
            except("]")
            return Color(r, g, b)
        }
    }
}