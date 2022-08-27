package com.skillw.pouvoir.internal.core.function.functions.common.math

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.util.NumberUtils.randomInt
import java.util.*

@AutoRegister
object FunctionRandomObj : PouFunction<Any>("randomObj") {
    override fun execute(parser: Parser): Any? {
        with(parser) {
            val token = parseString()
            if ((token.first() == '[')) {
                val list = LinkedList<Any>()
                while (hasNext()) {
                    except(",")
                    list.add(parse())
                    if (except("]")) break
                }
                return list[randomInt(0, list.size - 1)]
            }
        }
        return null
    }
}