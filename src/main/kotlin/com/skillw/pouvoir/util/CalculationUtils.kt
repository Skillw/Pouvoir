package com.skillw.pouvoir.util

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.internal.manager.PouvoirConfig
import com.skillw.pouvoir.util.StringUtils.replace
import org.bukkit.entity.LivingEntity
import taboolib.common.platform.function.warning
import java.math.BigDecimal
import java.util.*
import java.util.regex.Pattern

object CalculationUtils {

    @JvmStatic
    fun String.result(): BigDecimal {
        return getResult(this)
    }

    @JvmStatic
    fun String.result(entity: LivingEntity? = null, replacements: Map<String, String> = emptyMap()): BigDecimal {
        return getResult(this, entity, replacements)
    }

    @JvmStatic
    fun String.resultDouble(entity: LivingEntity? = null, replacements: Map<String, String> = emptyMap()): Double {
        return getResultDouble(this, entity, replacements)
    }

    @JvmStatic
    fun getResultDouble(
        formula: String,
        entity: LivingEntity? = null,
        replacements: Map<String, String> = emptyMap()
    ): Double {
        return getResult(formula, entity, replacements).setScale(PouvoirConfig.scale, BigDecimal.ROUND_HALF_UP)
            .toDouble()
    }

    @JvmStatic
    fun getResultDouble(
        formula: String
    ): Double {
        return getResultDouble(formula, null, emptyMap())
    }

    private fun doubleCal(a1: BigDecimal, a2: BigDecimal, operator: Char): BigDecimal {
        return when (operator) {
            '+' -> a1 + a2
            '-' -> a1 - a2
            '*' -> a1 * a2
            '/' -> a1.divide(a2, PouvoirConfig.scale, BigDecimal.ROUND_HALF_UP)
            '%' -> a1 % a2
            '^' -> a1.pow(a2.toInt())
            else -> {
                throw Exception("illegal operator!")
            }
        }
    }

    private fun getPriority(s: String?): Int {
        if (s == null) return 0
        return when (s) {
            "(" -> 1
            "+" -> {
                2
            }
            "-" -> 2
            "*" -> {
                3
            }
            "/" -> 3
            "%" -> 3
            else -> {
                throw Exception("illegal operator!")
            }
        }

    }

    private fun toSufExpr(expr: String): String {
        /*返回结果字符串*/
        val sufExpr = StringBuffer()
        /*盛放运算符的栈*/
        val operator = Stack<String?>()
        operator.push(null) //在栈顶压人一个null，配合它的优先级，目的是减少下面程序的判断
        /* 将expr打散分散成运算数和运算符 */
        val pattern = Pattern.compile("(?<!\\d)-?\\d+(\\.\\d+)?|[+\\-*/%()]") //这个正则为匹配表达式中的数字或运算符
        val matcher = pattern.matcher(expr)
        while (matcher.find()) {
            val temp = matcher.group()
            if (temp.matches(Regex("[+\\-*/%()]"))) { //是运算符
                when (temp) {
                    "(" -> { //遇到左括号，直接压栈
                        operator.push(temp)
                    }
                    ")" -> { //遇到右括号，弹栈输出直到弹出左括号（左括号不输出）
                        var topItem: String?
                        while (operator.pop().also { topItem = it } != "(") {
                            sufExpr.append(topItem).append(" ")
                        }
                    }
                    else -> { //遇到运算符，比较栈顶符号，若该运算符优先级大于栈顶，直接压栈；若小于栈顶，弹栈输出直到大于栈顶，然后将改运算符压栈。
                        while (getPriority(temp) <= getPriority(operator.peek())) {
                            sufExpr.append(operator.pop()).append(" ")
                        }
                        operator.push(temp)
                    }
                }
            } else { //遇到数字直接输出
                sufExpr.append(temp).append(" ")
            }
        }
        var topItem: String? //最后将符合栈弹栈并输出
        while (null != operator.pop().also { topItem = it }) {
            sufExpr.append(topItem).append(" ")
        }
        return sufExpr.toString()
    }

    @JvmStatic
    fun getResult(input: String): BigDecimal {
        try {
            val sufExpr = toSufExpr(input) // 转为后缀表达式
            /* 盛放数字栈 */
            val number = Stack<BigDecimal>()
            /* 这个正则匹配每个数字和符号 */
            val p = Pattern.compile("-?\\d+(\\.\\d+)?|[+\\-*/%^]")
            val m = p.matcher(sufExpr)
            while (m.find()) {
                val temp = m.group()
                if (temp.matches(Regex("[+\\-*/%^]"))) { // 遇到运算符，将最后两个数字取出，进行该运算，将结果再放入容器
                    val a1 = number.pop()
                    val a2 = number.pop()
                    val res = doubleCal(a2, a1, temp[0])
                    number.push(res)
                } else { // 遇到数字直接放入容器
                    number.push(BigDecimal(temp))
                }
            }
            return number.pop() ?: BigDecimal("0.0")
        } catch (e: Exception) {
            warning("计算式错误! $input")
            e.printStackTrace()
            return BigDecimal.valueOf(0.0)
        }
    }

    fun getResult(formula: String, entity: LivingEntity?, replacements: Map<String, String>? = null): BigDecimal {
        return getResult(Pouvoir.pouPlaceHolderAPI.replace(entity, replace(formula, replacements ?: HashMap())))
    }
}