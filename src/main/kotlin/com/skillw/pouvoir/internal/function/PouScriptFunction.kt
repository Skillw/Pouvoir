package com.skillw.pouvoir.internal.function

import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.Context
import com.skillw.pouvoir.api.function.reader.IReader

class PouScriptFunction(key: String, val path: String) : PouFunction<Any>(key) {
    override fun execute(reader: IReader, context: Context): Any? {
        return scriptManager
            .invoke(
                path,
                parameters = arrayOf(reader, context)
            )
    }

}