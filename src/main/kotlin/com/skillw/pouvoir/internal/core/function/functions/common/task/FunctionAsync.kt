package com.skillw.pouvoir.internal.core.function.functions.common.task

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import taboolib.common.platform.function.submitAsync
import taboolib.common.platform.service.PlatformExecutor

/**
 * @className FunctionBlock
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionAsync : PouFunction<PlatformExecutor.PlatformTask>(
    "async"
) {
    override fun execute(parser: Parser): PlatformExecutor.PlatformTask {
        with(parser) {
            var delay = 0L
            var period = 0L
            if (except("in")) {
                delay = parseLong()
            }
            if (except("every")) {
                period = parseLong()
            }
            val block = parseBlock()
            val asyncContext = context.clone()
            return submitAsync(delay == 0L, delay, period) {
                block.execute(asyncContext)
                context.putAllContext(asyncContext)
            }
        }
    }
}