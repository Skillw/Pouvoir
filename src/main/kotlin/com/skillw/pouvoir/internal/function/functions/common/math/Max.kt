package com.skillw.pouvoir.internal.function.functions.common.math

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.Context
import com.skillw.pouvoir.api.function.reader.IReader
import kotlin.math.max

@AutoRegister
object Max : PouFunction<Double>("max") {

    override fun execute(reader: IReader, context: Context): Double? {
        val x = reader.parseDouble(context) ?: return null
        val y = reader.parseDouble(context) ?: return null
        return max(x, y)
    }
}