package com.skillw.pouvoir.internal.core.function.functions.common.loop

import com.skillw.pouvoir.api.function.context.IContext
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.internal.core.function.context.SimpleContext
import com.skillw.pouvoir.internal.core.function.reader.SimpleReader
import java.util.*

object LoopUtils {
    fun runLoop(parser: Parser, loop: (IContext, () -> Boolean) -> Unit) {
        with(parser) {
            val label = if (except("label")) parseString() else UUID.randomUUID().toString()
            except("then")
            val process = splitTill("{", "}")
            val reader = SimpleReader(process)
            val loopContext = SimpleContext().apply {
                putAll(context)
                if (containsKey("loop-label")) {
                    (this.getOrPut("loop-label-in-${this["loop-label"].toString()}") { HashSet<String>() } as HashSet<String>).add(
                        label
                    )
                    put("in-loop", true)
                }
                this["loop-label"] = label
            }

            fun loopOnce(): Boolean {
                with(Parser.createParser(reader.reset(), loopContext)) {
                    while (hasNext()) {
                        if (context["loop-$label-break"] == true) return false
                        if (context["loop-$label-continue"] == true) {
                            context["loop-$label-continue"] = false
                            reset(); return true
                        }
                        parseAny()
                    }
                }
                return true
            }
            loop(loopContext) { loopOnce() }

            val inLoop = loopContext["in-loop"] == true
            loopContext.forEach { (key, value) ->
                context.computeIfPresent(key) { _, _ -> value }
                if (inLoop && key.startsWith("loop-"))
                    context[key] = value
            }
        }
    }
}