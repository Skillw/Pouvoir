package com.skillw.pouvoir.internal.core.function.functions.common.`object`

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import taboolib.library.reflex.Reflex.Companion.setProperty

@AutoRegister
object FunctionSetStatic : PouFunction<Any?>("setFieldStatic") {
    override fun execute(parser: Parser): Any? {
        with(parser) {
            val obj = parseAny()
            val fieldName = parseString()
            val value = parseAny()
            obj.setProperty(fieldName, value, findToParent = true)
            return value
        }
    }
}