package com.skillw.pouvoir.util

import com.skillw.pouvoir.api.fixed.FixedData
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.roundToLong

object NumberUtils {
    /**
     * To format a number with fractionDigits and integerDigits
     *
     * @param number    A number
     * @param fixedData FixedData
     * @return the number after format
     */
    @JvmOverloads
    @JvmStatic
    fun format(number: Number, fixedData: FixedData = FixedData.defaultData!!): Double {
        val fixedIntegerMax = fixedData.integerMax
        val fixedIntegerMin = fixedData.integerMin
        val fixedDecimalMax = fixedData.decimalMax
        val fixedDecimalMin = fixedData.decimalMin
        return format(number, fixedIntegerMax, fixedIntegerMin, fixedDecimalMax, fixedDecimalMin)
    }

    /**
     * To format a number with fractionDigits and integerDigits
     *
     * @param number          A number
     * @param fixedDecimalMax the maximum number of digits allowed in the integer portion of a number
     * @param fixedDecimalMin the minimum number of digits allowed in the integer portion of a number
     * @param fixedIntegerMax the maximum number of digits allowed in the fraction portion of a number
     * @param fixedIntegerMin the minimum number of digits allowed in the fraction portion of a number
     * @return the number after format
     */
    @JvmStatic
    fun format(
        number: Number,
        fixedIntegerMax: Int,
        fixedIntegerMin: Int,
        fixedDecimalMax: Int,
        fixedDecimalMin: Int
    ): Double {
        var fixedDecimalMax = fixedDecimalMax
        val value = number.toDouble()
        fixedDecimalMax = fixedDecimalMax.coerceAtLeast(fixedDecimalMin)
        if (fixedDecimalMax == 0) {
            return value.roundToInt().toDouble()
        }
        val numberFormat = NumberFormat.getNumberInstance()
        numberFormat.isGroupingUsed = false
        if (fixedIntegerMin != 0) {
            numberFormat.minimumIntegerDigits = fixedIntegerMin
        }
        if (fixedDecimalMin != 0) {
            numberFormat.minimumFractionDigits = fixedDecimalMin
        }
        if (fixedIntegerMax != -1) {
            numberFormat.maximumIntegerDigits = fixedIntegerMax
        }
        if (fixedDecimalMax != -1) {
            numberFormat.maximumFractionDigits = fixedDecimalMax
        }
        numberFormat.roundingMode = RoundingMode.HALF_UP
        return numberFormat.format(value).toDouble()
    }

    /**
     * To format a number
     *
     * @param number A number
     * @param format format
     * @return the number after format
     */
    @JvmStatic
    fun format(number: Number, format: String): String {
        val decimalFormat = DecimalFormat(format)
        return decimalFormat.format(number)
    }

    /**
     * To get a random double
     *
     * @param start the min value
     * @param end   the max value
     * @return A random double
     */
    @JvmOverloads
    @JvmStatic
    fun random(start: Number, end: Number, fixedData: FixedData = FixedData.defaultData!!): Double {
        val fixedIntegerMax = fixedData.integerMax
        val fixedIntegerMin = fixedData.integerMin
        val fixedDecimalMax = fixedData.decimalMax
        val fixedDecimalMin = fixedData.decimalMin
        val fixedDecimalValue = fixedData.decimalValue
        return random(start, end, fixedIntegerMax, fixedIntegerMin, fixedDecimalMax, fixedDecimalMin, fixedDecimalValue)
    }

    /**
     * To get a random double
     *
     * @param startNum        the min value
     * @param endNum          the max value
     * @param fixedDecimalMax the maximum number of digits allowed in the integer portion of a number
     * @param fixedDecimalMin the minimum number of digits allowed in the integer portion of a number
     * @param fixedIntegerMax the maximum number of digits allowed in the fraction portion of a number
     * @param fixedIntegerMin the minimum number of digits allowed in the fraction portion of a number
     * @param decimal         how many decimal places
     * @return A random double
     */
    @JvmStatic
    fun random(
        startNum: Number,
        endNum: Number,
        fixedIntegerMax: Int,
        fixedIntegerMin: Int,
        fixedDecimalMax: Int,
        fixedDecimalMin: Int,
        decimal: Int
    ): Double {
        val start = startNum.toDouble()
        val end = endNum.toDouble()
        return if (start > end) {
            max(start, 0.0)
        } else {
            val decimalNumber: Double = if (decimal != 0) format(
                Math.random(),
                -1,
                0,
                decimal,
                0
            ) else 0.0
            val x = (Math.random() * (end - start + 1) + start).roundToLong() + decimalNumber
            format(
                min(x, end),
                fixedIntegerMax,
                fixedIntegerMin,
                fixedDecimalMax,
                fixedDecimalMin
            )
        }
    }

    /**
     * To get a random integer
     *
     * @param start the min value
     * @param end   the max value
     * @return A random integer
     */
    @JvmStatic
    fun randomInt(start: Int, end: Int): Int {
        return if (start > end) {
            max(start, 0)
        } else {
            val x = (Math.random() * (end - start + 1) + start).roundToLong().toInt()
            if (x < 0) {
                0
            } else {
                min(x, end)
            }
        }
    }

    @JvmStatic
    fun getNumbers(numbers: String?, maxIndex: Int): List<Int> {
        val integers: MutableList<Int> = ArrayList()
        if (numbers != null && numbers.isNotEmpty()) {
            if (numbers.contains(",")) {
                val numbers1 = numbers.split(",").toTypedArray()
                for (number in numbers1) {
                    if (number != null && number.isNotEmpty()) {
                        handleNumbers(number, maxIndex, integers)
                    }
                }
            } else {
                handleNumbers(numbers, maxIndex, integers)
            }
        }
        return integers
    }

    @JvmStatic
    private fun handleNumbers(numbers: String, maxIndex: Int, integers: MutableList<Int>) {
        if (numbers.contains("-")) {
            for (j in numbers.split("-").toTypedArray()[0].toInt()..numbers.split("-").toTypedArray()[1].toInt()) {
                if (j <= maxIndex) {
                    integers.add(j)
                }
            }
        } else {
            val i = numbers.toInt()
            if (i <= maxIndex) {
                integers.add(i)
            }
        }
    }
}