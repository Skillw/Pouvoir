package com.skillw.pouvoir.api.function.reader


/**
 * I reader
 *
 * @constructor Create empty I reader
 */
interface IReader {
    val string: String

    /**
     * Has next
     *
     * @return
     */
    fun hasNext(): Boolean

    /**
     * Next
     *
     * @return
     */
    fun next(): String?

    /**
     * Previous
     *
     * @return
     */
    fun previous(): String?

    fun currentIndex(): Int

    fun current(): String

    fun peek(): String?


    fun skipTill(reader: IReader, from: String, till: String): Boolean
    fun splitTill(reader: IReader, from: String, to: String): String?

    fun reset(): IReader

    fun currentLine(): Int

    fun exit()

    fun currentLineIndex(): Int
    fun peekNextIgnoreBlank(): String?
}

