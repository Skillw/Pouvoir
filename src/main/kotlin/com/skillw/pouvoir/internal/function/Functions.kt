package com.skillw.pouvoir.internal.function

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.internal.manager.PouConfig
import com.skillw.pouvoir.util.CalculationUtils.calculateDouble
import com.skillw.pouvoir.util.MessageUtils.warning
import com.skillw.pouvoir.util.NumberUtils.format
import com.skillw.pouvoir.util.NumberUtils.randomInt
import taboolib.common.util.random
import taboolib.common5.Coerce
import taboolib.common5.RandomList
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.*

private fun `if`(x: String, symbol: String, y: String): Boolean {
    return when (symbol) {
        "<" -> Coerce.toDouble(x) < Coerce.toDouble(y)
        "<=" -> Coerce.toDouble(x) <= Coerce.toDouble(y)
        "==" -> Coerce.toDouble(x) == Coerce.toDouble(y)
        "!=" -> Coerce.toDouble(x) != Coerce.toDouble(y)
        "===" -> x === y
        "!==" -> x !== y
        ">" -> Coerce.toDouble(x) > Coerce.toDouble(y)
        ">=" -> Coerce.toDouble(x) >= Coerce.toDouble(y)
        "equals" -> x == y
        "!equals" -> x != y
        "contains" -> x.contains(y)
        "!contains" -> !x.contains(y)
        "equalsIgnore" -> x.lowercase() == y.lowercase()
        "!equalsIgnore" -> x.lowercase() != y.lowercase()
        else -> {
            warning("Unknown symbol $symbol!")
            false
        }
    }
}

@AutoRegister
object If : PouFunction("if") {
    override fun predicate(args: Array<String>): Boolean {
        return args.size == 5 || args.size == 9
    }

    override fun function(args: Array<String>): Any {
        when (args.size) {
            5 -> {
                val x = args[0]
                val symbol = args[1]
                val y = args[2]
                val trueValue = args[3]
                val falseValue = args[4]
                val compare = `if`(x, symbol, y)
                return if (compare) trueValue else falseValue
            }

            9 -> {
                val bool1 = `if`(args[0], args[1], args[2])
                val bool2 = `if`(args[4], args[5], args[6])
                val trueValue = args[7]
                val falseValue = args[8]
                val bool = when (args[3]) {
                    "&&" -> bool1 && bool2
                    "||" -> bool1 || bool2
                    "^" -> bool1 && !bool2
                    else -> {
                        warning("Unknown logical operator ${args[3]}!")
                        false
                    }
                }
                return if (bool) trueValue else falseValue
            }

            else -> {
                return "Wrong arguments!"
            }
        }
    }
}

@AutoRegister
object Abs : PouFunction("abs") {
    override fun predicate(args: Array<String>): Boolean {
        return args.size == 1 && Coerce.asDouble(args[0]).isPresent
    }

    override fun function(args: Array<String>): Any? {
        val number = Coerce.toDouble(args[0])
        return abs(number)
    }
}

@AutoRegister
object Ceil : PouFunction("ceil") {
    override fun predicate(args: Array<String>): Boolean {
        return args.size == 1 && Coerce.asDouble(args[0]).isPresent
    }

    override fun function(args: Array<String>): Any? {
        val number = Coerce.toDouble(args[0])
        return ceil(number)
    }
}

@AutoRegister
object Format : PouFunction("format") {
    override fun predicate(args: Array<String>): Boolean {
        return args.size == 2 && Coerce.asDouble(args[0]).isPresent
    }

    override fun function(args: Array<String>): Any? {
        val number = Coerce.toDouble(args[0])
        val format = args[1]
        return number.format(format)
    }
}

@AutoRegister
object Floor : PouFunction("floor") {
    override fun predicate(args: Array<String>): Boolean {
        return args.size == 1 && Coerce.asDouble(args[0]).isPresent
    }

    override fun function(args: Array<String>): Any? {
        val number = Coerce.toDouble(args[0])
        return floor(number)
    }
}

@AutoRegister
object Max : PouFunction("max") {
    override fun predicate(args: Array<String>): Boolean {
        return args.size == 2 && Coerce.asDouble(args[0]).isPresent && Coerce.asDouble(args[1]).isPresent
    }

    override fun function(args: Array<String>): Any? {
        val x = Coerce.toDouble(args[0])
        val y = Coerce.toDouble(args[1])
        return max(x, y)
    }
}

@AutoRegister
object Min : PouFunction("min") {
    override fun predicate(args: Array<String>): Boolean {
        return args.size == 2 && Coerce.asDouble(args[0]).isPresent && Coerce.asDouble(args[1]).isPresent
    }

    override fun function(args: Array<String>): Any? {
        val x = Coerce.toDouble(args[0])
        val y = Coerce.toDouble(args[1])
        return min(x, y)
    }
}

@AutoRegister
object Random : PouFunction("random") {
    override fun predicate(args: Array<String>): Boolean {
        return args.size >= 2 && Coerce.asDouble(args[0]).isPresent && Coerce.asDouble(args[1]).isPresent
    }

    override fun function(args: Array<String>): Any? {
        val x = Coerce.toDouble(args[0])
        val y = Coerce.toDouble(args[1])
        val format = if (args.size == 3) args[2] else PouConfig.numberFormat
        return random(x, y).format(format)
    }
}

@AutoRegister
object RandomInt : PouFunction("randomInt") {
    override fun predicate(args: Array<String>): Boolean {
        return args.size >= 2 && Coerce.asInteger(args[0]).isPresent && Coerce.asInteger(args[1]).isPresent
    }

    override fun function(args: Array<String>): Any? {
        val x = Coerce.toInteger(args[0])
        val y = Coerce.toInteger(args[1])
        return randomInt(x, y)
    }
}

@AutoRegister
object Round : PouFunction("round") {
    override fun predicate(args: Array<String>): Boolean {
        return args.size == 1 && Coerce.asDouble(args[0]).isPresent
    }

    override fun function(args: Array<String>): Any? {
        val x = Coerce.toDouble(args[0])
        return round(x)
    }
}

@AutoRegister
object Calculate : PouFunction("calculate") {
    override fun predicate(args: Array<String>): Boolean {
        return args.isNotEmpty() && (args.size == 1 || args.filterIndexed { index, _ -> index != 0 }
            .all { it1 -> it1.matches(Regex(".*=.*")) })
    }

    override fun function(args: Array<String>): Any? {
        val formula = args[0]
        val replaced = ConcurrentHashMap<String, String>()
        for (index in 1..args.lastIndex) {
            val str = args[index]
            val key = str.split("=")[0]
            val value = str.replace("$key=", "")
            replaced[key] = value
        }
        return formula.replace("[", "(").replace("]", ")").calculateDouble(null, replaced)
    }
}

@AutoRegister
object Repeat : PouFunction("repeat") {
    override fun predicate(args: Array<String>): Boolean {
        return args.size >= 2 && Coerce.asInteger(args[0]).isPresent
    }

    override fun function(args: Array<String>): Any? {
        val time = Coerce.toInteger(args[0])
        var value = ""
        for (index in args.indices) {
            if (index == 0) continue
            value += args[index]
            if (index != 1) {
                value += ","
            }
        }
        var result = ""
        repeat(time) {
            result += value
        }
        return result
    }
}

@AutoRegister
object Weight : PouFunction(
    "weight"
) {
    override fun predicate(args: Array<String>): Boolean {
        return args.isNotEmpty() && args.all { arg -> arg.contains("::") }
    }

    override fun function(args: Array<String>): Any? {
        val weightRandom = RandomList<String>()
        args.forEach {
            val weight = Coerce.toInteger(it.split("::")[0])
            val value = it.replace("$weight::", "")
            weightRandom.add(value, weight)
        }
        return weightRandom.random()
    }
}



