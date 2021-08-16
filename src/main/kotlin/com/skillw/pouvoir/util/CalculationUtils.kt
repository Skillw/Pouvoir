package com.skillw.pouvoir.util

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.util.NumberUtils.format
import org.bukkit.entity.LivingEntity
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object CalculationUtils {
    @JvmStatic
    fun replace(formula: String, replaceds: ConcurrentHashMap<String, String>): String {
        var formula = formula
        for (key in replaceds.keys) {
            formula = formula.replace(key, replaceds[key]!!)
        }
        return formula
    }

    @JvmStatic
    fun getResult(input: String): Double {
        //规范输入形式,避免用户输入中文括号
        var input = input
        input = input.replace("（".toRegex(), "(")
        input = input.replace("）".toRegex(), ")")
        //对输入公式,按符号/数字,用空格分开,以便后面分组
        val inputs = input.split("").toTypedArray()
        val format = StringBuilder()
        for (i in inputs.indices) {
            if (" " == inputs[i]) {
            } else if ("(" == inputs[i] || ")" == inputs[i] || "+" == inputs[i] || "-" == inputs[i] || "*" == inputs[i] || "/" == inputs[i]) {
                format.append(" ").append(inputs[i]).append(" ")
            } else {
                format.append(inputs[i])
            }
        }
        val strings = changeInfixExpressionToPostfixExpression(format.toString())
        val stack = Stack<String>()
        for (string in strings) {
            if ("+" == string) {
                val a = BigDecimal(stack.pop())
                val b = BigDecimal(stack.pop())
                stack.add(b.add(a).toString())
            } else if ("-" == string) {
                val a = BigDecimal(stack.pop())
                val b = BigDecimal(stack.pop())
                stack.add(b.subtract(a).toString())
            } else if ("*" == string) {
                val a = BigDecimal(stack.pop())
                val b = BigDecimal(stack.pop())
                stack.add(b.multiply(a).toString())
            } else if ("/" == string) {
                val a = BigDecimal(stack.pop())
                val b = BigDecimal(stack.pop())
                //这里的1000是做除法以后计算的精确位数,就算1000位也并不会拖慢程序速度,一个公式0.01秒内就能算完,后面的是除不尽的四舍五入
                stack.add(b.divide(a, 1000, BigDecimal.ROUND_HALF_DOWN).toString())
            } else {
                stack.add(string)
            }
        }
        //返回的时候格式化一下,取四舍五入小数点后两位
        return format(BigDecimal(stack.pop()).toDouble())
    }

    private fun changeInfixExpressionToPostfixExpression(input: String): List<String> {
        val resultList: MutableList<String> = ArrayList()
        val tempStack = Stack<String>()
        val splitArray = input.split(" ").toTypedArray()
        for (i in splitArray.indices) {
            if ("" == splitArray[i]) {
                continue
            }
            //如果字符是右括号的话,说明前面一定有过左括号,将栈里第一个左括号之前全部添加到List里
            if (")" == splitArray[i]) {
                while ("(" != tempStack.peek()) {
                    resultList.add(tempStack.pop())
                }
                tempStack.pop() //去除前面的左括号
            } else if ("(" == splitArray[i]) {
                //如果是左括号,那么直接添加进去
                tempStack.add("(")
            } else if ("+" == splitArray[i] || "-" == splitArray[i]) {
                //如果是加减号,还需要再判断
                if (tempStack.empty() || "(" == tempStack.peek()) {
                    tempStack.add(splitArray[i])
                } else if ("+" == tempStack.peek() || "-" == tempStack.peek()) {
                    //读临时栈里的顶部数据,如果也是加减就取出来一个到结果列,这个放临时栈,如果是乘除就开始取到右括号为止
                    resultList.add(tempStack.pop())
                    tempStack.add(splitArray[i])
                } else {
                    while (!tempStack.empty()) {
                        if ("(" == tempStack.peek()) {
                            break
                        } else {
                            resultList.add(tempStack.pop())
                        }
                    }
                    tempStack.add(splitArray[i])
                }
            } else if ("*" == splitArray[i] || "/" == splitArray[i]) {
                //如果是乘除
                if (!tempStack.empty()) {
                    //判断临时栈里栈顶是啥,如果是乘除,取一个出来到结果列,添这个进临时栈
                    if ("*" == tempStack.peek() || "/" == tempStack.peek()) {
                        resultList.add(tempStack.pop())
                    }
                }
                tempStack.add(splitArray[i])
            } else {
                //说明是非符号,都添加进去
                resultList.add(splitArray[i])
            }
        }
        //遍历完了,把临时stack里的东西都加到结果stack里去
        while (!tempStack.empty()) {
            resultList.add(tempStack.pop())
        }
        return resultList
    }

    fun getResult(formula: String, entity: LivingEntity?, replacements: ConcurrentHashMap<String, String>): Double {
        return getResult(replace(Pouvoir.rpgPlaceHolderAPI.replace(entity, formula), replacements))
    }
}