package com.skillw.pouvoir.internal.function.functions.common.loop

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser

@AutoRegister
object FunctionContinue : PouFunction<Unit>("continue") {
    override fun execute(parser: Parser) {
        with(parser) {
            if (!context.containsKey("loop-label")) return
            val label = if (except("the")) parseString() else context["loop-label"] as String
            context["$label-continue"] = true
        }
    }
}