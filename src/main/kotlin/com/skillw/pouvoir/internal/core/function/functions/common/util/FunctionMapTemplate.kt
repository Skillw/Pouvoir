package com.skillw.pouvoir.internal.core.function.functions.common.util

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.internal.core.function.util.MapTemplate

/**
 * @className FunctionMapTemplate
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionMapTemplate : PouFunction<MapTemplate>(
    "mapTemplate"
) {
    override fun execute(parser: Parser): MapTemplate {
        with(parser) {
            return MapTemplate(parseList().map { it.toString() })
        }
    }
}