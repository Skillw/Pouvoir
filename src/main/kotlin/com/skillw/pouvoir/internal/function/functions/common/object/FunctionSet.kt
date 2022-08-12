package com.skillw.pouvoir.internal.function.functions.common.`object`

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser
import taboolib.library.reflex.Reflex.Companion.setProperty

@AutoRegister
object FunctionSet : PouFunction<Any?>("setField") {
    override fun execute(parser: Parser): Any? {
        with(parser) {
            val obj = parseAny() ?: return null
            val fieldName = parseString()
            val value = parseAny()
            obj.setProperty(fieldName, value, findToParent = true)
            return value
        }
    }
}