package com.skillw.pouvoir.internal.core.function.functions.common.task

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.internal.core.function.context.SimpleContext
import taboolib.common.util.sync

/**
 * @className FunctionBlock
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionSync : PouFunction<Any>(
    "sync"
) {
    override fun execute(parser: Parser): Any? {
        with(parser) {
            val block = parseBlock()
            val syncContext = context.clone()
            return sync {
                val result = block.execute(syncContext)
                context.putAllContext(syncContext)
                result
            }
        }
    }
}