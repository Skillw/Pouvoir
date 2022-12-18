package com.skillw.pouvoir.internal.core.function.reader

import com.skillw.pouvoir.api.function.reader.IReader
import com.skillw.pouvoir.api.map.BaseMap
import java.util.*

/**
 * SimpleReader
 *
 * @param string
 * @constructor
 */
open class SimpleReader : IReader {
    companion object {
        private val cache = BaseMap<Int, SimpleReader>()

        fun create(string: String): SimpleReader {
            return SimpleReader(cache.getOrPut(string.hashCode()) { SimpleReader(string) })
        }
    }

    private val splits: LinkedList<String>
    private var count = -1
    private var line = 1
    private var lineIndex = -1

    private val str: String
    override val string: String
        get() = str

    protected constructor(string: String) {
        this.str = string
        splits =
            LinkedList(string.replace("\n", " \n ").split(" ").filter { it == "\n" || (it.isNotEmpty() && it != " ") })
        var notes = false
        for (index in splits.indices) {
            val str = splits[index]
            val previous = splits.getOrNull(index - 1)
            if (str.startsWith("/*") && str.endsWith("*/")) {
                splits.removeAt(index)
                continue
            }
            if (notes) {
                splits.removeAt(index)
            }
            if (notes && str == "\n") {
                notes = false
                continue
            }
            if (str == "#" && (previous == "\n" || previous == null)) {
                notes = true
                splits.removeAt(index)
            }
        }
    }

    constructor(simpleReader: SimpleReader) {
        this.str = simpleReader.str
        splits = LinkedList(simpleReader.splits)
    }


    /**
     * Has next
     *
     * @return
     */
    override fun hasNext(): Boolean {
        return count + 1 < splits.size
    }

    override fun current(): String {
        return splits[count]
    }

    /**
     * Next
     *
     * @return
     */
    override fun next(): String? {
        if (!hasNext()) {
            return null
        }
        lineIndex++
        return splits[++count].let {
            if (it == "\n") {
                line++
                lineIndex = -1
                next()
            } else it
        }
    }

    override fun previous(): String? {
        if (count - 1 <= 0) {
            return null
        }
        return splits[--count].let {
            if (it == "\n") {
                line--
                //previous不维护lineIndex
                lineIndex = -1
                previous()
            } else it
        }
    }

    override fun currentIndex(): Int {
        return count
    }

    override fun peekNextIgnoreBlank(): String? {
        return splits.getOrNull(count + 1)?.run {
            ifBlank {
                count++
                return peekNextIgnoreBlank().also { count-- }
            }
        }
    }

    override fun peek(): String? {
        return splits.getOrNull(count + 1)
    }

    override fun skipTill(reader: IReader, from: String, till: String): Boolean {
        var countIf = 0
        while (reader.hasNext()) {
            when (reader.next() ?: return false) {
                from -> countIf++
                till -> if (--countIf <= 0) return true
                else -> {}
            }
        }
        return false
    }

    override fun splitTill(reader: IReader, from: String, to: String): String? {
        var count = 0
        val builder = StringBuilder()
        while (reader.hasNext()) {
            when (reader.next() ?: return null) {
                from -> if (count++ == 0) continue
                to -> if (--count <= 0) return builder.toString()
            }
            builder.append(" ${reader.current()}")
        }
        return builder.toString()
    }

    override fun reset(): IReader {
        count = -1
        line = 1
        lineIndex = -1
        return this
    }

    override fun currentLine(): Int {
        return line
    }

    override fun currentLineIndex(): Int {
        return lineIndex
    }

    override fun exit() {
        count = splits.size - 1
    }

}