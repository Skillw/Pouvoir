package com.skillw.pouvoir.util

import com.skillw.pouvoir.util.plugin.Pair
import taboolib.common.util.asList
import java.util.*


private fun String.subStringWithEscape(from: Int, to: Int, escapes: List<Int>): String {
    val builder = StringBuilder()
    if (escapes.isEmpty())
        return substring(from, to)
    val it = escapes.iterator()
    var currentFrom = from
    var currentTo = it.next()
    while (currentTo != to) {
        builder.append(currentFrom, currentTo)
        currentFrom = currentTo + 1
        currentTo = if (it.hasNext())
            it.next()
        else
            to
    }
    if (currentFrom != currentTo)
        builder.append(currentFrom, currentTo)
    return builder.toString()
}


fun String.protectedSplit(index: Char, protector: Pair<Char, Char>): ArrayList<String> {
    val list = ArrayList<String>()
    var inner = false
    var startIndex = 0
    val len = this.length
    val escapes = ArrayList<Int>()
    for (endIndex in 0 until len) {
        val c = this[endIndex]
        if (inner) {
            if (c == protector.value && this[endIndex - 1] != '\\') {
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

                protector.key -> {
                    if (this[endIndex - 1] != '\\') {
                        inner = true
                        escapes.add(endIndex)
                    }
                }
            }
        }
    }
    if (startIndex < len)
        list.add(subStringWithEscape(startIndex, len, escapes))
    return list
}


fun Any.toStringWithNext(): String {
    if (this is Collection<*>) {
        return this.toStringWithNext()
    }
    return this.toString()
}


fun String.toObjList(): List<String> {
    return if (this.contains("\n")) {
        this.split("\n").asList()
    } else listOf(this)
}


fun Collection<*>.toStringWithNext(): String {
    return this.joinToString("\n")
}


fun String.toArgs(): Array<String> =

    if (this.contains(","))
        split(",").toTypedArray()
    else
        arrayOf(this)


fun String.replacement(replaces: Map<String, Any>): String {
    var formulaCopy = this
    replaces.forEach {
        formulaCopy = formulaCopy.replace(it.key, it.value.toString())
    }
    return formulaCopy
}


fun String.replacementIntRange(replaces: Map<IntRange, Any>): String {
    var formulaCopy = this
    replaces.forEach {
        formulaCopy = formulaCopy.replaceRange(it.key, it.value.toString())
    }
    return formulaCopy
}


fun <T> Collection<T>.replacement(replaces: Map<String, Any>): List<String> {
    val list = LinkedList<String>()
    for (it in this) {
        list.add(it.toString().replacement(replaces))
    }
    return list
}


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
        val arrayList = ArrayList<String>()
        for (i in indexes[0]..indexes[1]) {
            arrayList.add(strList[i])
        }
        arrayList.toStringWithNext()
    } else {
        "NULL"
    }
}

@JvmName("parse1")

fun String.parse(leftChar: Char = '(', rightChar: Char = ')'): List<String> {
    val text = this
    val stack = Stack<Int>()
    var left = false
    val list = ArrayList<String>()
    for (index in text.indices) {
        val char = text[index]
        if (char == leftChar) {
            if (left) {
                stack.pop()
                stack.push(index)
            } else {
                left = true
                stack.push(index)
            }
        }
        if (char == rightChar) {
            if (left) {
                val start = stack.pop()
                list.add(text.substring(start + 1 until index))
                left = false
            }
        }
    }
    return list
}


internal fun String.format(): String {
    return this.replace(Regex("\\s+"), " ")
}
