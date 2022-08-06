package com.skillw.pouvoir.internal.function.functions.common

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.Context
import com.skillw.pouvoir.api.function.reader.IReader
import java.util.*


@AutoRegister
object Join : PouFunction<String>(
    "join"
) {
    override fun execute(reader: IReader, context: Context): String? {
        with(reader) {
            val list = LinkedList<String>()
            if (!except("[")) return null
            while (hasNext()) {
                val any = parseAny(context) ?: return null
                list.add(any.toString())
                if (except("]")) break
            }
            if (!except("by")) return null
            val by = parseString(context) ?: return null
            return list.joinToString(by)
        }
    }
}



