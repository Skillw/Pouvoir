package com.skillw.pouvoir.internal.core.function.functions.common.util

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.internal.core.function.util.MapTemplate

/**
 * @className FunctionMapOf
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionMapOf : PouFunction<MutableMap<String, Any>>(
    "mapOf"
) {
    override fun execute(parser: Parser): MutableMap<String, Any> {
        with(parser) {
            return if (except("with")) {
                val template = parse<MapTemplate>()
                return template.build(parseList())
            } else parseMap()
        }
    }
}