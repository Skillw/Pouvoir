package com.skillw.pouvoir.internal.function.functions.common.math

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.Context
import com.skillw.pouvoir.api.function.reader.IReader
import taboolib.common.util.random

@AutoRegister
object Random : PouFunction<Double>("random") {
    override fun execute(reader: IReader, context: Context): Double? {
        with(reader) {
            val x = parseDouble(context) ?: return null
            except("to")
            val y = parseDouble(context) ?: return null
            return random(x, y)
        }
    }
}