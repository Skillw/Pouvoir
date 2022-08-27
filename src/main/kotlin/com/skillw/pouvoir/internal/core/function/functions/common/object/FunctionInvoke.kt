package com.skillw.pouvoir.internal.core.function.functions.common.`object`

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.IFunction
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import java.util.*

@AutoRegister
object FunctionInvoke : PouFunction<Any?>("invoke") {
    override fun execute(parser: Parser): Any? {
        with(parser) {
            val obj = parseAny()
            if (obj is IFunction<*>) {
                return obj.execute(parser)
            }
            val methodName = parseString()
            if (!except("[")) {
                return obj.invokeMethod(methodName, findToParent = true)
            }
            val parameters = LinkedList<Any?>()
            while (hasNext()) {
                parameters.add(parseNext())
                except(",")
                if (except("]")) break
            }
            return obj.invokeMethod(methodName, *parameters.toTypedArray(), findToParent = true)
        }
    }
}