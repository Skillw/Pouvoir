package com.skillw.pouvoir.internal.core.function.functions.common.util

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser

/**
 * @className FunctionListOf
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionPairOf : PouFunction<Pair<Any, Any>>(
    "pairOf"
) {
    override fun execute(parser: Parser): Pair<Any, Any> {
        with(parser) {
            return parse()
        }
    }
}