package com.skillw.pouvoir.internal.function.functions.common.math

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.Context
import com.skillw.pouvoir.api.function.reader.IReader
import kotlin.math.round

@AutoRegister
object Round : PouFunction<Int>("round") {

    override fun execute(reader: IReader, context: Context): Int? {
        val x = reader.parseDouble(context) ?: return null
        return round(x).toInt()
    }
}