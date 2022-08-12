package com.skillw.pouvoir.internal.function.functions.common.loop

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser

@AutoRegister
object FunctionBreak : PouFunction<Unit>("break") {
    override fun execute(parser: Parser) {
        with(parser) {
            if (!context.containsKey("loop-label")) return
            val label = if (except("the")) parseString() else context["loop-label"] as String
            context["$label-break"] = true
        }
    }
}