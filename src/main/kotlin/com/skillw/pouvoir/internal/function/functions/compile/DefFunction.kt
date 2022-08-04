package com.skillw.pouvoir.internal.function.functions.compile

import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.Context
import com.skillw.pouvoir.api.function.reader.IReader
import com.skillw.pouvoir.internal.function.common.PouFunc
import com.skillw.pouvoir.internal.function.reader.FileReader
import com.skillw.pouvoir.util.StringUtils.toArgs

object DefFunction : PouFunction<PouFunc>("fun", "compile") {
    override fun execute(reader: IReader, context: Context): PouFunc? {
        if (reader !is FileReader) return null
        val name = reader.next() ?: return null
        val paramsName = name.substring(name.indexOf('(') + 1, name.indexOf(')')).toArgs()
        if (!reader.except("=")) return null
        val content = subBigBrackets(reader) ?: return null
        return PouFunc(name, paramsName, content).also { reader.functions.register(it) }
    }

    private fun subBigBrackets(reader: IReader): String? {
        val builder = StringBuilder()
        with(reader) {
            while (hasNext()) {
                val next = next() ?: return null
                when (next) {
                    "{" -> {}
                    "}" -> return builder.toString()
                    else -> builder.append(next)
                }
            }
        }
        return null
    }
}

/**
 * fun name(params) {
 *
 * }
 */