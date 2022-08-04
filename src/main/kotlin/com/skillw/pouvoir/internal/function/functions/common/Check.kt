package com.skillw.pouvoir.internal.function.functions.common


import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.Context
import com.skillw.pouvoir.api.function.reader.IReader
import taboolib.common.platform.function.warning
import taboolib.common5.Coerce

@AutoRegister
object Check : PouFunction<Boolean>("check") {

    private fun Any?.toDouble() = Coerce.toDouble(this)

    private fun check(x: Any?, symbol: String, y: Any?): Boolean {
        return when (symbol) {
            "<" -> x.toDouble() < y.toDouble()
            "<=" -> x.toDouble() <= y.toDouble()
            "==" -> x.toDouble() == y.toDouble()
            "!=" -> x != y
            "===" -> x === y
            "!==" -> x !== y
            ">" -> x.toDouble() > y.toDouble()
            ">=" -> x.toDouble() >= y.toDouble()
            "equals" -> x == y
            "!equals" -> x != y
            "in" -> x.toString() in y.toString()
            "!in" -> x.toString() !in y.toString()
            "is" -> if (y is Class<*>) y.isInstance(x) else false
            "!is" -> if (y is Class<*>) !y.isInstance(x) else false
            "contains" -> x.toString().contains(y.toString())
            "!contains" -> !x.toString().contains(y.toString())
            "equalsIgnore" -> x.toString().lowercase() == y.toString().lowercase()
            "!equalsIgnore" -> x.toString().lowercase() != y.toString().lowercase()
            else -> {
                warning("Unknown symbol $symbol!")
                false
            }
        }
    }


    override fun execute(reader: IReader, context: Context): Boolean? {
        with(reader) {
            val a = parseAny(context) ?: return null
            val symbol = parseAny(context) ?: return null
            val b = parseAny(context) ?: return null
            return check(a, symbol.toString(), b)
        }
    }
}