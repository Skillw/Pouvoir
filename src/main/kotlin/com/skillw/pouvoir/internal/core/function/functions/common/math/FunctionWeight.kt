package com.skillw.pouvoir.internal.core.function.functions.common.math

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import taboolib.common5.RandomList


@AutoRegister
object FunctionWeight : PouFunction<Any>(
    "weight"
) {
    override fun execute(parser: Parser): Any? {
        val weightRandom = RandomList<Any>()
        with(parser) {
            if (!except("[")) return null
            while (hasNext()) {
                val weight = parseInt()
                except("to")
                val any = parseAny()
                weightRandom.add(any, weight)
                except(",")
                if (except("]")) break
            }
        }
        return weightRandom.random()
    }
}



