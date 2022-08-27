package com.skillw.pouvoir.internal.core.function.functions.common.loop

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser

@AutoRegister
object FunctionBreak : PouFunction<Unit>("break") {
    override fun execute(parser: Parser) {
        with(parser) {
            if (!context.containsKey("loop-label")) return
            val label = if (except("the")) parseString() else context["loop-label"] as String
            context["loop-$label-break"] = true
            if (containsKey("loop-label-in-$label")) {
                (context["loop-label-in-$label"] as Collection<String>).forEach {
                    context["loop-$it-break"] = true
                }
            }
        }
    }
}