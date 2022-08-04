package com.skillw.pouvoir.internal.function.functions.common.math

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.Context
import com.skillw.pouvoir.api.function.reader.IReader
import com.skillw.pouvoir.util.NumberUtils.format

@AutoRegister
object Format : PouFunction<String>("format") {

    override fun execute(reader: IReader, context: Context): String? {
        val number = reader.parseDouble(context) ?: return null
        val format = reader.parseString(context) ?: return null
        return number.format(format)
    }
}