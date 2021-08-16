package com.skillw.pouvoir.util

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
}