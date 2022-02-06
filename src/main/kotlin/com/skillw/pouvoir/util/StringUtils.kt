package com.skillw.pouvoir.util

import taboolib.common.util.asList
import java.util.*
import java.util.regex.Pattern

object StringUtils {
    private fun String.subStringWithEscape(from: Int, to: Int, escapes: List<Int>): String {
        val builder = StringBuilder()
        if (escapes.isEmpty())
            return substring(from, to)
        val it = escapes.iterator()
        var currentfrom = from
        var currentto = it.next()
        while (currentto != to) {
            builder.append(currentfrom, currentto)
            currentfrom = currentto + 1
            currentto = if (it.hasNext())
                it.next()
            else
                to
        }
        if (currentfrom != currentto)
            builder.append(currentfrom, currentto)
        return builder.toString()
    }

    @JvmStatic
    fun String.protectedSplit(index: Char, protector: Pair<Char, Char>): ArrayList<String> {
        val list = ArrayList<String>()
        var inner = false
        var startIndex = 0
        val len = this.length
        val escapes = ArrayList<Int>()
        for (endIndex in 0 until len) {
            val c = this[endIndex]
            if (inner) {
                if (c == protector.second) {
                    inner = false
                    escapes.add(endIndex)
                }
            } else {
                when (c) {
                    index -> {
                        list.add(subStringWithEscape(startIndex, endIndex, escapes))
                        escapes.clear()
                        startIndex = endIndex + 1
                    }
                    protector.first -> {
                        inner = true
                        escapes.add(endIndex)
                    }
                }
            }
        }
        if (startIndex < len)
            list.add(subStringWithEscape(startIndex, len, escapes))
        return list
    }

    @JvmStatic
    fun Any.toStringWithNext(): String {
        if (this is Collection<*>) {
            return this.toStringWithNext()
        }
        return this.toString()
    }

    @JvmStatic
    fun Collection<*>.toStringWithNext(): String {
        return this.toString().replace(", ", "\n").replace("[", "").replace("]", "")
    }

    @JvmStatic
    fun String.toList(): List<String> {
        return if (this.contains("\n")) {
            this.split("\n").asList()
        } else listOf(this)
    }

    @JvmStatic
    fun String.toArgs(): Array<String> =
        if (this.contains(","))
            this.replace(" ", "").split(",").toTypedArray()
        else
            arrayOf(this.replace(" ", ""))

    @JvmStatic
    fun String.replacement(replaces: Map<String, Any>): String {
        return replace(this, replaces)
    }

    @JvmStatic
    fun <T> Collection<T>.replacement(replaces: Map<String, Any>): List<String> {
        val list = LinkedList<String>()
        for (it in this) {
            list.add(it.toString().replacement(replaces))
        }
        return list
    }


    @JvmStatic
    fun replace(formula: String, replaces: Map<String, Any>): String {

        var formulaCopy = formula
        for (key in replaces.keys) {
            val value = replaces[key]!!
            if (value is String) {
                formulaCopy = formulaCopy.replace(key, value)
            } else if (value is List<*>) {
                val pattern = Pattern.compile("$key\\((.*)\\)")
                var matcher = pattern.matcher(formulaCopy)
                while (matcher.find()) {
                    val args = matcher.group(1)?.toArgs() ?: continue
                    formulaCopy = formulaCopy.replace(key, value.asList().getMultiple(args))
                    matcher = pattern.matcher(formulaCopy)
                }
            }
        }
        return formulaCopy
    }

    @JvmStatic
    fun List<String>.getMultiple(args: Array<String>): String {
        val strList = this
        val indexes: IntArray = when (args.size) {
            1 -> intArrayOf(
                args[0].toInt()
            )
            2 -> intArrayOf(
                args[0].toInt(),
                args[1].toInt()
            )
            else -> intArrayOf()
        }
        return if (indexes.isEmpty()) {
            strList.toStringWithNext()
        } else if (indexes.size == 1) {
            strList[indexes[0]] + "\n"
        } else if (indexes.size == 2) {
            val arrayList = java.util.ArrayList<String>()
            for (i in indexes[0]..indexes[1]) {
                arrayList.add(strList[i])
            }
            arrayList.toStringWithNext()
        } else {
            "NULL"
        }
    }


}