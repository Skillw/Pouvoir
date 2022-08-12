package com.skillw.pouvoir.internal.function.functions.common.`object`

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import java.util.*

@AutoRegister
object FunctionInvokeStatic : PouFunction<Any?>("invokeStatic") {
    override fun execute(parser: Parser): Any? {
        with(parser) {
            val obj = parseAny() ?: return null
            val methodName = parseString()
            if (!except("[")) {
                return obj.invokeMethod(methodName, isStatic = true, findToParent = true)
            }
            val parameters = LinkedList<Any?>()
            while (hasNext()) {
                parameters.add(parseAny())
                if (except(",")) continue
                else if (except("]")) break
            }
            return obj.invokeMethod(methodName, *parameters.toTypedArray(), isStatic = true, findToParent = true)
        }
    }
}