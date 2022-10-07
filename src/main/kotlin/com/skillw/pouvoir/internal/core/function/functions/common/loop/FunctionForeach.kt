package com.skillw.pouvoir.internal.core.function.functions.common.loop

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser


@AutoRegister
object FunctionForeach : PouFunction<Unit>(
    "foreach",
) {
    override fun execute(parser: Parser) {

        with(parser) {
            val paramName = next() ?: return
            except("in")
            val collection = parseAny()
            if (collection !is Array<*> && collection !is Collection<*>) error("parse Collection or Array error")

            LoopUtils.runLoop(parser) { loopContext, loopOnce ->
                when (collection) {
                    is Array<*> -> {
                        for (item in collection) {
                            loopContext[paramName] = item ?: continue
                            if (!loopOnce()) break
                        }
                    }

                    is Collection<*> -> {
                        for (item in collection) {
                            loopContext[paramName] = item ?: continue
                            if (!loopOnce()) break
                        }
                    }
                }
            }


        }
    }
}
