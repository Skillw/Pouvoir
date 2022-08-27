package com.skillw.pouvoir.internal.core.function.functions.common.loop

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import taboolib.common5.Coerce


@AutoRegister
object FunctionWhile : PouFunction<Unit>(
    "while"
) {
    override fun execute(parser: Parser) {

        with(parser) {
            val condition = parseBlock()
            LoopUtils.runLoop(parser) { loopContext, loopOnce ->
                while (Coerce.toBoolean(condition.execute(loopContext))) if (!loopOnce()) break
            }
        }
    }
}
