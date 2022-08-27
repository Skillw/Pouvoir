package com.skillw.pouvoir.internal.core.function.functions.common.task

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.internal.core.function.context.SimpleContext
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor

/**
 * @className FunctionBlock
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionTask : PouFunction<PlatformExecutor.PlatformTask>(
    "task"
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
            val syncContext = SimpleContext(context)
            return submit(now = delay == 0L, async = false, delay = delay, period = period) {
                block.execute(syncContext)
                context.putAll(syncContext)
            }
        }
    }
}