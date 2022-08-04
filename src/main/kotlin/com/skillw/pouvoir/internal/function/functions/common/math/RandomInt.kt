package com.skillw.pouvoir.internal.function.functions.common.math

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.Context
import com.skillw.pouvoir.api.function.reader.IReader
import com.skillw.pouvoir.util.NumberUtils

@AutoRegister
object RandomInt : PouFunction<Int>("randomInt") {

    override fun execute(reader: IReader, context: Context): Int? {
        with(reader) {
            val x = parseInt(context) ?: return null
            except("to")
            val y = parseInt(context) ?: return null
            return NumberUtils.randomInt(x, y)
        }
    }
}