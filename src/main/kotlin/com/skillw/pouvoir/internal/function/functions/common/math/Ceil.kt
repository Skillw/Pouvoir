package com.skillw.pouvoir.internal.function.functions.common.math

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.Context
import com.skillw.pouvoir.api.function.reader.IReader
import kotlin.math.ceil

@AutoRegister
object Ceil : PouFunction<Double>("ceil") {
    override fun execute(reader: IReader, context: Context): Double? {
        val number = reader.parseDouble(context) ?: return null
        return ceil(number)
    }
}