package com.skillw.pouvoir.internal.core.function.functions.common.loop

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser


@AutoRegister
object FunctionRepeat : PouFunction<Unit>(
    "repeat"
) {
    override fun execute(parser: Parser) {

        with(parser) {
            val time = parseInt()
            val index = if (except("with")) parseString() else "index"
            LoopUtils.runLoop(parser) { loopContext, loopOnce ->
                for (i in 0 until time) {
                    loopContext[index] = i
                    if (!loopOnce()) break
                }
            }

        }
    }
}
