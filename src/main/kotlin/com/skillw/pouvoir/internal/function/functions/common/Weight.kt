package com.skillw.pouvoir.internal.function.functions.common

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.Context
import com.skillw.pouvoir.api.function.reader.IReader
import taboolib.common5.RandomList


@AutoRegister
object Weight : PouFunction<Any>(
    "weight"
) {
    override fun execute(reader: IReader, context: Context): Any? {
        val weightRandom = RandomList<Any>()
        with(reader) {
            while (hasNext()) {
                val weight = parseInt(context) ?: return null
                if (!except("to")) return null
                val value = parseAny(context) ?: return null
                weightRandom.add(value, weight)
            }
        }
        return weightRandom.random()
    }
}



