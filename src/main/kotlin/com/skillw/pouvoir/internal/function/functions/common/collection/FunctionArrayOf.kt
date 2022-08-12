package com.skillw.pouvoir.internal.function.functions.common.collection

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser

/**
 * @className FunctionBlock
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