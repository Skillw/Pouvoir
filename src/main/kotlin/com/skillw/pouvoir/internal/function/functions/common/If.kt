package com.skillw.pouvoir.internal.function.functions.common


import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.Context
import com.skillw.pouvoir.api.function.reader.IReader
import taboolib.common.platform.function.warning
import taboolib.common5.Coerce

@AutoRegister
object If : PouFunction<Any>("if") {

    private fun Any.toBool() = Coerce.toBoolean(this)

    override fun execute(reader: IReader, context: Context): Any? {
        with(reader) {
            var bool = parseBoolean(context) ?: return null
            val next = next() ?: return bool
            when (next) {
                "||" -> if (!bool) bool =
                    parseBoolean(context).also { if (it == true) except("then") }
                        ?: return null else skipTill(reader, "then")

                "&&" -> if (bool) bool =
                    parseBoolean(context).also { if (it == true) except("then") } ?: return null

                "then" -> {}

                else -> {
                    warning("Unknown token $next!")
                    return null
                }
            }
            if (bool) {
                val result = parseAny(context)
                next()
                parseAny(context)
                return result
            } else {
                if (!skipTill(reader, "else")) return null
                return parseAny(context)
            }
        }
    }


    private fun skipTill(reader: IReader, till: String? = null): Boolean {
        var countIf = 0
        while (reader.hasNext()) {
            when (reader.next() ?: return false) {
                "if" -> countIf++
                till -> if (--countIf <= 0) return true
                else -> {}
            }
        }
        return false
    }
}