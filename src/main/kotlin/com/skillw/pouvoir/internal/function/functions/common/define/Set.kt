package com.skillw.pouvoir.internal.function.functions.common.define

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.Context
import com.skillw.pouvoir.api.function.reader.IReader

@AutoRegister
object Set : PouFunction<Any>("set") {
    override fun execute(reader: IReader, context: Context): Any? {
        with(reader) {
            val key = reader.parseString(context) ?: return null
            except("to")
            val value = reader.parseAny(context)
            value?.let { context[key] = it } ?: context.remove(key)
            return value
        }
    }
}