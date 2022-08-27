package com.skillw.pouvoir.internal.core.function.reader

import com.skillw.pouvoir.api.function.reader.IReader
import java.util.*

/**
 * SimpleReader
 *
 * @param string
 * @constructor
 */
open class SimpleReader : IReader {
    private val splits: List<String>
    private var count = -1

    private val str: String
    override val string: String
        get() = str

    constructor(string: String) {
        this.str = string
        splits = string.replace("\n", " \n ").split(" ").filter { it.isNotEmpty() && it != " " }
    }

    constructor(simpleReader: SimpleReader) {
        this.str = simpleReader.str
        splits = LinkedList(simpleReader.splits)
        count = -1
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
        return splits[++count]
    }

    override fun previous(): String? {
        if (count - 1 <= 0) {
            return null
        }
        return splits[--count]
    }

    override fun currentIndex(): Int {
        return count
    }

    override fun peekNext(): String? {
        if (!hasNext()) {
            return null
        }
        return splits[count + 1]
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
        var countIf = 0
        val builder = StringBuilder()
        while (reader.hasNext()) {
            when (reader.next() ?: return null) {
                from -> countIf++
                to -> if (--countIf <= 0) return builder.toString()
            }
            builder.append(" ${reader.current()}")
        }
        return builder.toString()
    }

    override fun reset(): IReader {
        count = -1
        return this
    }

    override fun exit() {
        count = splits.size - 1
    }

}