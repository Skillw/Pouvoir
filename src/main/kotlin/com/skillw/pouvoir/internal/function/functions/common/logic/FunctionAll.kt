package com.skillw.pouvoir.internal.function.functions.common.logic

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser
import taboolib.common5.Coerce

/**
 * @className FunctionBlock
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionAll : PouFunction<Boolean>(
    "all"
) {
    override fun execute(parser: Parser): Boolean {
        with(parser) {
            return parseArray().all { Coerce.toBoolean(it) }
        }
    }
}