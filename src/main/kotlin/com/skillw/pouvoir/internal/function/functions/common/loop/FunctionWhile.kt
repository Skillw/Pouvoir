package com.skillw.pouvoir.internal.function.functions.common.loop

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser
import com.skillw.pouvoir.internal.function.context.SimpleContext
import com.skillw.pouvoir.internal.function.reader.SimpleReader
import java.util.*


@AutoRegister
object FunctionWhile : PouFunction<Unit>(
    "while"
) {
    override fun execute(parser: Parser) {

        with(parser) {
            val condition = parser.parseString()
            val conditionReader = SimpleReader(condition)

            val label = if (parser.except("label")) {
                parser.parseString()
            } else {
                UUID.randomUUID().toString()
            }

            val process = parser.splitTill("{", "}")
            val reader = SimpleReader(process)
            val loopContext = SimpleContext().apply {
                putAll(parser.context)
                this["loop-label"] = label
            }

            fun loopOnce(): Boolean {
                with(Parser(reader.reset(), loopContext)) {
                    while (hasNext()) {
                        if (context["$label-break"] == true) return false
                        if (context["$label-continue"] == true) {
                            reset(); return true
                        }
                        parseAny()
                    }
                }
                return true
            }

            while (Parser(conditionReader.reset(), loopContext).parseBoolean()) {
                if (!loopOnce()) break
            }

            context.forEach { (key, value) ->
                parser.context.computeIfPresent(key) { _, _ -> value }
            }
        }
    }
}
