package com.skillw.pouvoir.internal.function.functions.common.logic


import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser
import taboolib.common.platform.function.warning
import taboolib.common5.Coerce

@AutoRegister
object FunctionCheck : PouFunction<Boolean>("check") {

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
            "in" -> when (y) {
                is Collection<*> -> x in y
                else -> x.toString() in y.toString()
            }

            "!in" -> when (y) {
                is Collection<*> -> x !in y
                else -> x.toString() !in y.toString()
            }

            "is" -> when (y) {
                is Class<*> -> y.isInstance(x)
                else -> false
            }

            "!is" -> when (y) {
                is Class<*> -> !y.isInstance(x)
                else -> false
            }

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


    override fun execute(parser: Parser): Boolean? {
        with(parser) {
            val a = parseAny()
            val symbol = parseAny()
            val b = parseAny()
            return check(a, symbol.toString(), b)
        }
    }
}