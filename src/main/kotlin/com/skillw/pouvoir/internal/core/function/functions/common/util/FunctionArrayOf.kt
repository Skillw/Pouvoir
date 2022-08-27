package com.skillw.pouvoir.internal.core.function.functions.common.util

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser

/**
 * @className FunctionArrayOf
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionArrayOf : PouFunction<Array<Any>>(
    "arrayOf"
) {
    override fun execute(parser: Parser): Array<Any> {
        with(parser) {
            return parseArray()
        }
    }
}