package com.skillw.pouvoir.internal.function.functions.common.math

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.Context
import com.skillw.pouvoir.api.function.reader.IReader
import kotlin.math.min

@AutoRegister
object Min : PouFunction<Double>("min") {
    override fun execute(reader: IReader, context: Context): Double? {
        val x = reader.parseDouble(context) ?: return null
        val y = reader.parseDouble(context) ?: return null
        return min(x, y)
    }
}