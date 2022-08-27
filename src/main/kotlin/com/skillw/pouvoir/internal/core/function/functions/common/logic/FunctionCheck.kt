package com.skillw.pouvoir.internal.core.function.functions.common.logic


import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
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
            "equalsIgnore" -> x.toString().equals(y.toString(), true)
            "!equalsIgnore" -> !x.toString().equals(y.toString(), true)
            else -> {
                warning("Unknown symbol $symbol!")
                false
            }
        }
    }


    override fun execute(parser: Parser): Boolean? {
        with(parser) {
            val a = parseAny()
            val symbol = parseString()
            val b = parseAny()
            return check(a, symbol, b)
        }
    }
}