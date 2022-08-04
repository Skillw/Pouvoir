package com.skillw.pouvoir.internal.function.functions.common.math

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.Context
import com.skillw.pouvoir.api.function.reader.IReader
import kotlin.math.floor

@AutoRegister
object Floor : PouFunction<Double>("floor") {

    override fun execute(reader: IReader, context: Context): Double? {
        val number = reader.parseDouble(context) ?: return null
        return floor(number)
    }
}