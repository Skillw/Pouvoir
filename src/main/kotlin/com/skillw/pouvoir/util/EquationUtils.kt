package com.skillw.pouvoir.util

import com.skillw.pouvoir.util.MessageUtils.wrong

/**
 * ClassName : com.skillw.classsystem.util.EquationUtils
 * Created by Glom_ on 2021-03-25 21:13:38
 * Copyright  2021 user. All rights reserved.
 */
object EquationUtils {
    /**
     * 此类方法用于将字符串化为: aX+b 结构，结果返回a,b
     *
     * @param str:需要计算的字符串
     */
    private fun translate(str: String): Result? {
        var strCopy = str
        strCopy = deleteKH(strCopy)
        val chars = strCopy.toCharArray()
        //先根据 + 号 将字符串分割完
        run {
            var i = 0
            var kuHaoNum = 0
            while (i < chars.size) {
                kuHaoNum = getBracketsNum(kuHaoNum, i, chars)
                if (kuHaoNum == 0 && "+" == chars[i].toString() + "") { //括号里面不分割
                    val s01 = strCopy.substring(0, i)
                    val s02 = strCopy.substring(i + 1, strCopy.length)
                    val result01 = getResultFromString(s01)
                    val result02 = getResultFromString(s02)
                    val result = Result()
                    result.a = result01!!.a + result02!!.a
                    result.b = result01.b + result02.b
                    return result
                }
                i++
            }
        }
        //再根据 - 号 将字符串分割完  注意：- 号是从后往前分割
        run {
            var i = chars.size - 1
            var kuHaoNum = 0
            while (i >= 0) {
                kuHaoNum = getBracketsNum(kuHaoNum, i, chars)
                if (kuHaoNum == 0 && "-" == chars[i].toString() + "") { //括号里面不分割
                    val s01 = strCopy.substring(0, i)
                    val s02 = strCopy.substring(i + 1, strCopy.length)
                    val result01 = getResultFromString(s01)
                    val result02 = getResultFromString(s02)
                    val result = Result()
                    result.a = result01!!.a - result02!!.a
                    result.b = result01.b - result02.b
                    return result
                }
                i--
            }
        }
        //最后根据 *，/ 号 将字符串分割
        var i = 0
        var kuHaoNum = 0
        while (i < chars.size) {
            kuHaoNum = getBracketsNum(kuHaoNum, i, chars)
            if (kuHaoNum == 0 && ("*" == chars[i].toString() + "" || "/" == chars[i].toString() + "")) { //括号里面不分割
                val s01 = strCopy.substring(0, i)
                val fuhao = strCopy.substring(i, i + 1)
                val s02 = strCopy.substring(i + 1, strCopy.length)
                val result01 = getResultFromString(s01)
                val result02 = getResultFromString(s02)
                val result = Result()
                if (fuhao == "*") {  //因为是一元一次方程  不会出现 （aX+b）*(aX+b)的情况
                    if (result01!!.a != 0.0) {
                        result.a = result01.a * result02!!.b
                        result.b = result01.b * result02.b
                    }
                    if (result02!!.a != 0.0) {
                        result.a = result01.b * result02.a
                        result.b = result01.b * result02.b
                    }
                    if (result01.a == 0.0 && result02.a == 0.0) {
                        result.a = 0.0
                        result.b = result01.b * result02.b
                    }
                } else if (fuhao == "/") {
                    result.a = result01!!.a / result02!!.b
                    result.b = result01.b / result02.b
                }
                return result
            }
            i++
        }
        return null
    }

    /**
     * 此类方法用于获取从起始位置到当前位置经历了几个括号
     *
     * @param num:括号数量
     * @param index:字符数组的位置
     * @param chars:字符数组
     */
    private fun getBracketsNum(num: Int, index: Int, chars: CharArray): Int {
        var numCopy = num
        if ("(" == chars[index].toString() + "") {
            numCopy++
        } else if (")" == chars[index].toString() + "") {
            numCopy--
        }
        return numCopy
    }

    /**
     * 此类方法用于将字符串转化为 aX+b 的形式  返回 Result
     *
     * @param str:需要转化的字符串
     */
    private fun getResultFromString(str: String): Result? {
        var result: Result? = Result()
        if (str == "") {
            result!!.a = 0.0
            result.b = 0.0
        } else if (isRightString(str)) {
            var standString = getStandardFormatString(str)
            standString = deleteKH(standString)
            result!!.a = standString.substring(0, standString.indexOf("x")).toDouble()
            result.b = standString.substring(standString.indexOf("x") + 1, standString.length).toDouble()
        } else if (isNum(str)) {
            result!!.a = 0.0
            result.b = str.toDouble()
        } else {
            result = translate(str)
        }
        return result
    }

    @JvmStatic
    fun calculate(string: String): Double {
        val string01 = string.substring(0, string.indexOf("="))
        val string02 = string.substring(string.indexOf("=") + 1)
        val result01 = translate(string01)
        val result02 = translate(string02)
        val a1 = result01!!.a
        val b1 = result01.b
        val a2 = result02!!.a
        val b2 = result02.b
        if (a1 == a2 && b1 != b2) {
            wrong("Equation &e$string &chas no solution")
        } else if (a1 != a2) {
            return (b2 - b1) / (a1 - a2)
        }
        return 0.0
    }

    /**
     * 此类方法用于判断字符串是否是数字
     *
     * @param str:需要判断的字符串
     */
    private fun isNum(str: String): Boolean {
        var isNum = true
        try {
            str.toDouble()
        } catch (e: Exception) {
            isNum = false
        }
        return isNum
    }

    /**
     * 此类方法用于判断字符串是否是类似于  aX+b 格式
     *
     * @param s:需要判断的字符串
     */
    private fun isRightString(s: String): Boolean {
        val c = s.toCharArray()
        var j = 0
        for (i in c.indices) {
            if ("+" == c[i].toString() + "" || "-" == c[i].toString() + "") {
                j++
            }
        }
        if (s.contains("x") && j <= 1) {
            if (!s.contains("(")) {
                return true
            } else if (s.contains("(") && s.indexOf("(") == 0) {
                return true
            }
        }
        return false
    }

    /**
     * 此类方法用于将字符串转化为  aX+b 标准格式
     *
     * @param s:需要转化的字符串
     */
    private fun getStandardFormatString(s: String): String {
        val c = s.toCharArray()
        var t = 0
        val s1 = StringBuffer(s)
        return if (s.contains("+") || s.contains("-")) {
            for (i in c.indices) {
                if ("+" == c[i].toString() + "" || "-" == c[i].toString() + "") {
                    t = i //t 用于记住 +,-号的位置
                    break
                }
            }
            if (s.indexOf("x") < t) {
                s
            } else {
                val s2 = s.substring(0, t)
                val s3 = s.substring(t, s.length)
                val ss = StringBuffer(s3)
                ss.append(s2)
                ss.toString()
            }
        } else {
            s1.append("+0").toString()
        }
    }

    /**
     * 此类方法用于去掉字符串最外层的括号
     *
     * @param str:需要处理的字符串
     */
    private fun deleteKH(str: String): String {
        var strCopy = str
        if (strCopy.startsWith("(") && strCopy.endsWith(")")) {
            strCopy = strCopy.substring(1, strCopy.length - 1)
        }
        return strCopy
    }

    /**
     * 此类定义了  aX+b 结构中的 a和b
     * 注意 这里的a,b可以为负,即aX-b也属于这种结构
     */
    private class Result {
        var a = 0.0
        var b = 0.0

        constructor() {}
        constructor(a: Double, b: Double) {
            this.a = a
            this.b = b
        }

        override fun toString(): String {
            return "Result{" +
                    "a=" + a +
                    ", b=" + b +
                    '}'
        }
    }
}