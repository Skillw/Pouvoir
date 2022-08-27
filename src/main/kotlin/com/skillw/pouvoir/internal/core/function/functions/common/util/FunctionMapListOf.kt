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
object FunctionMapListOf : PouFunction<MutableList<MutableMap<String, Any>>>(
    "mapListOf"
) {
    override fun execute(parser: Parser): MutableList<MutableMap<String, Any>>? {
        with(parser) {
            return if (except("with")) {
                val template = parse<MapTemplate>()
                return parseList().mapNotNull { template.build(it as? List<Any>? ?: return@mapNotNull null) }
                    .toMutableList()
            } else null
        }
    }
}