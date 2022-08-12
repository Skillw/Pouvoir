package com.skillw.pouvoir.internal.function.functions.common.import

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser
import com.skillw.pouvoir.util.ClassUtils.findClass
import com.skillw.pouvoir.util.ClassUtils.static

@AutoRegister
object FunctionImport : PouFunction<Any?>("import") {
    override fun execute(parser: Parser): Any? {
        with(parser) {
            val className = parseString()
            return className.findClass()?.static()
        }
    }
}