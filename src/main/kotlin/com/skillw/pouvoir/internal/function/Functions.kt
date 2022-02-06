package com.skillw.pouvoir.internal.function

import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.internal.manager.PouvoirConfig
import com.skillw.pouvoir.util.CalculationUtils
import com.skillw.pouvoir.util.MessageUtils.wrong
import com.skillw.pouvoir.util.NumberUtils.format
import com.skillw.pouvoir.util.NumberUtils.randomInt
import taboolib.common.util.random
import taboolib.common5.Coerce
import taboolib.common5.RandomList
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.*

private fun If(x: String, symbol: String, y: String): Boolean {
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
            wrong("Unknown symbol $symbol!")
            false
        }
    }
}

object If : PouFunction("if",
    {
        it.size == 5 || it.size == 9
    }, func@{ args ->
        when (args.size) {
            5 -> {
                val x = args[0]
                val symbol = args[1]
                val y = args[2]
                val trueValue = args[3]
                val falseValue = args[4]
                val compare = If(x, symbol, y)
                return@func if (compare) trueValue else falseValue
            }
            9 -> {
                val bool1 = If(args[0], args[1], args[2])
                val bool2 = If(args[4], args[5], args[6])
                val trueValue = args[7]
                val falseValue = args[8]
                val bool = when (args[3]) {
                    "&&" -> bool1 && bool2
                    "||" -> bool1 || bool2
                    "^" -> bool1 && !bool2
                    else -> {
                        wrong("Unknown logical operator ${args[3]}!")
                        false
                    }
                }
                return@func if (bool) trueValue else falseValue
            }
            else -> {
                return@func "Wrong arguments!"
            }
        }
    })

object abs : PouFunction("abs",
    {
        it.size == 1 && Coerce.asDouble(it[0]).isPresent
    }, func@{ args ->
        val number = Coerce.toDouble(args[0])
        return@func abs(number)
    })

object ceil : PouFunction("ceil",
    {
        it.size == 1 && Coerce.asDouble(it[0]).isPresent
    }, func@{ args ->
        val number = Coerce.toDouble(args[0])
        return@func ceil(number)
    })

object format : PouFunction("format",
    {
        it.size == 2 && Coerce.asDouble(it[0]).isPresent
    }, func@{ args ->
        val number = Coerce.toDouble(args[0])
        val format = args[1]
        return@func number.format(format)
    })

object floor : PouFunction("floor",
    {
        it.size == 1 && Coerce.asDouble(it[0]).isPresent
    }, func@{ args ->
        val number = Coerce.toDouble(args[0])
        return@func floor(number)
    })

object max : PouFunction("max",
    {
        it.size == 2 && Coerce.asDouble(it[0]).isPresent && Coerce.asDouble(it[1]).isPresent
    }, func@{ args ->
        val x = Coerce.toDouble(args[0])
        val y = Coerce.toDouble(args[1])
        return@func max(x, y)
    })

object min : PouFunction("min",
    {
        it.size == 2 && Coerce.asDouble(it[0]).isPresent && Coerce.asDouble(it[1]).isPresent
    }, func@{ args ->
        val x = Coerce.toDouble(args[0])
        val y = Coerce.toDouble(args[1])
        return@func min(x, y)
    })

object random : PouFunction("random",
    {
        it.size >= 2 && Coerce.asDouble(it[0]).isPresent && Coerce.asDouble(it[1]).isPresent
    }, func@{ args ->
        val x = Coerce.toDouble(args[0])
        val y = Coerce.toDouble(args[1])
        val format = if (args.size == 3) args[2] else PouvoirConfig.numberFormat
        return@func random(x, y).format(format)
    })

object randomInt : PouFunction("randomInt",
    {
        it.size >= 2 && Coerce.asInteger(it[0]).isPresent && Coerce.asInteger(it[1]).isPresent
    }, func@{ args ->
        val x = Coerce.toInteger(args[0])
        val y = Coerce.toInteger(args[1])
        return@func randomInt(x, y)
    })

object round : PouFunction("round",
    {
        it.size == 1 && Coerce.asDouble(it[0]).isPresent
    }, func@{ args ->
        val x = Coerce.toDouble(args[0])
        return@func round(x)
    })

object calculate : PouFunction("calculate",
    {
        it.isNotEmpty() && (it.size == 1 || it.filterIndexed { index, _ -> index != 0 }
            .all { it1 -> it1.matches(Regex(".*=.*")) })
    }, func@{ args ->
        val formula = args[0]
        val replaced = ConcurrentHashMap<String, String>()
        for (index in 1..args.lastIndex) {
            val str = args[index]
            val key = str.split("=")[0]
            val value = str.replace("$key=", "")
            replaced[key] = value
        }
        return@func CalculationUtils.getResult(formula, null, replaced)
    })

object repeat : PouFunction("repeat",
    {
        it.size >= 2 && Coerce.asInteger(it[0]).isPresent
    }, func@{ args ->
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
        return@func result
    })

object weight : PouFunction("weight",
    {
        it.isNotEmpty() && it.all { arg -> arg.contains("::") }
    }, func@{ args ->
        val weightRandom = RandomList<String>()
        args.forEach {
            val weight = Coerce.toInteger(it.split("::")[0])
            val value = it.replace("$weight::", "")
            weightRandom.add(value, weight)
        }
        return@func weightRandom.random()
    })



