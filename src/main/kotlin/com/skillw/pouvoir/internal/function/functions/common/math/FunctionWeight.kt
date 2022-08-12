package com.skillw.pouvoir.internal.function.functions.common.math

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser
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
                val any = parseAny() ?: continue
                weightRandom.add(any, weight)
                if (except(",")) continue
                else if (except("]")) break
            }
        }
        return weightRandom.random()
    }
}



