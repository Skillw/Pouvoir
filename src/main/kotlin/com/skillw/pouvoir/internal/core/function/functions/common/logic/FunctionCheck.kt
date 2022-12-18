package com.skillw.pouvoir.internal.core.function.functions.common.logic


import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.util.ClassUtils.instanceof
import taboolib.common.platform.function.warning
import taboolib.common5.Coerce

@AutoRegister
object FunctionCheck : PouFunction<Boolean>("check") {

    private fun Any?.toDouble() = Coerce.toDouble(this)
    private fun Any?.isNumber() = Coerce.asDouble(this).isPresent

    @JvmStatic
    fun check(a: Any?, symbol: String, b: Any?): Boolean {
        return when (symbol) {
            "<" -> a.toDouble() < b.toDouble()
            "<=" -> a.toDouble() <= b.toDouble()
            "==" -> when{
                a.isNumber() && b.isNumber() -> a.toDouble() == b.toDouble()
                else -> a == b
            }
            "!=" -> when{
                a.isNumber() && b.isNumber() -> a.toDouble() != b.toDouble()
                else -> a != b
            }
            "===" -> a === b
            "!==" -> a !== b
            ">" -> a.toDouble() > b.toDouble()
            ">=" -> a.toDouble() >= b.toDouble()
            "equals" -> a == b
            "!equals" -> a != b
            "in" -> when (b) {
                is Collection<*> -> a in b
                //                  内联函数range的返回值就是ClosedRange<Double> 所以直接强转了
                is ClosedRange<*> -> (b as ClosedRange<Double>).contains(a.toDouble())
                else -> a.toString() in b.toString()
            }

            "!in" -> when (b) {
                is Collection<*> -> a !in b
                //                  内联函数range的返回值就是ClosedRange<Double> 所以直接强转了
                is ClosedRange<*> -> !(b as ClosedRange<Double>).contains(a.toDouble())
                else -> a.toString() !in b.toString()
            }

            "is" -> when {
                b is Class<*> -> b.isInstance(a)
                else -> b?.let { a?.instanceof(it) } == true
            }

            "!is" -> when (b) {
                is Class<*> -> !b.isInstance(a)
                else -> b?.let { a?.instanceof(it) } == false
            }

            "contains" -> a.toString().contains(b.toString())
            "!contains" -> !a.toString().contains(b.toString())
            "equalsIgnore" -> a.toString().equals(b.toString(), true)
            "!equalsIgnore" -> !a.toString().equals(b.toString(), true)
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