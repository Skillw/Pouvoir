package com.skillw.pouvoir.internal.core.function.functions.common.math

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import taboolib.common5.Coerce
import kotlin.math.max

@AutoRegister
object FunctionMax : PouFunction<Double>("max") {

    override fun execute(parser: Parser): Double {
        with(parser) {
            when (val next = parseAny()) {
                is List<*> -> {
                    val numbers = next.map { Coerce.toDouble(it) }
                    return numbers.maxOf { it }
                }

                else -> {
                    val a = Coerce.toDouble(next)
                    except("to")
                    val b = parseDouble()
                    return max(a, b)
                }
            }

        }
    }
}