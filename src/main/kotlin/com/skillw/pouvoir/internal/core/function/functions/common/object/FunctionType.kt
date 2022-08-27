package com.skillw.pouvoir.internal.core.function.functions.common.`object`

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import taboolib.common5.Coerce

@AutoRegister
object FunctionType : PouFunction<Any?>("type") {
    override fun execute(parser: Parser): Any? {
        with(parser) {
            val type = parseString().lowercase()
            val obj = parseAny()
            return when (type) {
                "double" -> Coerce.toDouble(obj)
                "int" -> Coerce.toInteger(obj)
                "long" -> Coerce.toLong(obj)
                "float" -> Coerce.toFloat(obj)
                "short" -> Coerce.toShort(obj)
                "byte" -> Coerce.toByte(obj)
                "bool" -> Coerce.toBoolean(obj)
                "char" -> Coerce.toChar(obj)
                "string" -> Coerce.toString(obj)
                else -> obj
            }
        }
    }
}