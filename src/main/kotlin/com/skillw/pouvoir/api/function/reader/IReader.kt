package com.skillw.pouvoir.api.function.reader

import com.skillw.pouvoir.api.function.context.Context


/**
 * I reader
 *
 * @constructor Create empty I reader
 */
interface IReader {
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
    fun parseAny(context: Context): Any?
    fun parseString(context: Context): String?
    fun parseInt(context: Context): Int?
    fun parseLong(context: Context): Long?
    fun parseFloat(context: Context): Float?
    fun parseDouble(context: Context): Double?
    fun parseBoolean(context: Context): Boolean?
    fun parseByte(context: Context): Byte?
    fun parseShort(context: Context): Short?
    fun parseArray(context: Context): Array<Any>?


    fun except(except: String): Boolean
    fun current(): String
}

/**
 * Parse once
 *
 * @param context
 * @return
 */
inline fun <reified T> IReader.parse(context: Context): T? {
    return when (T::class.java) {
        String::class.java -> parseString(context) as T?
        Int::class.java -> parseInt(context) as T?
        Long::class.java -> parseLong(context) as T?
        Float::class.java -> parseFloat(context) as T?
        Double::class.java -> parseDouble(context) as T?
        Boolean::class.java -> parseBoolean(context) as T?
        Byte::class.java -> parseByte(context) as T?
        Short::class.java -> parseShort(context) as T?
        Array<Any>::class.java -> parseArray(context) as T?
        else -> parseAny(context) as T?
    }
}