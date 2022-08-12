package com.skillw.pouvoir.internal.function.functions.common.`object`

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser
import taboolib.library.reflex.Reflex.Companion.getProperty

@AutoRegister
object FunctionGetStatic : PouFunction<Any?>("getFieldStatic") {
    override fun execute(parser: Parser): Any? {
        with(parser) {
            val obj = parseAny() ?: return null
            val fieldName = parseString()
            return obj.getProperty(fieldName, findToParent = true, isStatic = true)
        }
    }
}