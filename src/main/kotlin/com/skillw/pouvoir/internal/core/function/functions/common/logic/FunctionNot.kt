package com.skillw.pouvoir.internal.core.function.functions.common.logic

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser

/**
 * @className FunctionNot
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionNot : PouFunction<Boolean>(
    "not"
) {
    override fun execute(parser: Parser): Boolean {
        with(parser) {
            return !parseBoolean()
        }
    }
}