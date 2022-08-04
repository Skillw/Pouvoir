package com.skillw.pouvoir.internal.function.reader

import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.Context
import com.skillw.pouvoir.api.function.reader.IReader
import taboolib.common5.Coerce
import java.util.*

/**
 * SimpleReader
 *
 * @param string
 * @constructor
 */
open class SimpleReader(string: String) : IReader {
    private val splits = string.replace("\n", " ").split(" ").filter { it.isNotEmpty() && it.isNotBlank() }
    private var count = -1

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

    fun peekNext(): String? {
        if (!hasNext()) {
            return null
        }
        return splits[count + 1]
    }

    override fun parseAny(context: Context): Any? {
        val token = next() ?: return null
        if (token == "true") return true
        if (token == "false") return false
        if (token == "pass") return ""
        if (token == "null") return null
        if ((token.first() == '\'')) {
            if (token.last() == '\'') return token.replace("\'", "".replace("_", " "))
            val builder = StringBuilder(token.replace("\'", ""))
            while (hasNext()) {
                if (next()?.last() != '\'')
                    builder.append(" " + current())
                else {
                    builder.append(" " + current().replace("\'", ""))
                    break
                }
            }
            return builder.toString()
        }
        if (token.startsWith("&")) {
            return context[token.replace("&", "")]
        }
        val function = context[token].run { if (this is PouFunction<*>) this else return token }
        return function.execute(this, context)
    }

    override fun parseString(context: Context): String? {
        return parseAny(context)?.toString()
    }

    override fun parseInt(context: Context): Int? {
        return Coerce.asInteger(parseAny(context)).run { if (isPresent) get() else null }
    }

    override fun parseLong(context: Context): Long? {
        return Coerce.asLong(parseAny(context)).run { if (isPresent) get() else null }
    }

    override fun parseFloat(context: Context): Float? {
        return Coerce.asFloat(parseAny(context)).run { if (isPresent) get() else null }
    }

    override fun parseDouble(context: Context): Double? {
        return Coerce.asDouble(parseAny(context)).run { if (isPresent) get() else null }
    }

    override fun parseBoolean(context: Context): Boolean? {
        return Coerce.asBoolean(parseAny(context)).run { if (isPresent) get() else null }
    }

    override fun parseByte(context: Context): Byte? {
        return Coerce.asByte(parseAny(context)).run { if (isPresent) get() else null }
    }

    override fun parseShort(context: Context): Short? {
        return Coerce.asShort(parseAny(context)).run { if (isPresent) get() else null }
    }

    override fun parseArray(context: Context): Array<Any>? {
        val token = next() ?: return null
        if (token == "null") return null
        if (context.containsKey(token)) return context[token]?.run {
            if (this is PouFunction<*>) return this.execute(
                this@SimpleReader,
                context
            ) as Array<Any>? else return this as Array<Any>?
        }
        if ((token.first() == '[')) {
            val list = LinkedList<Any>()
            while (peekNext() != null) {
                if (peekNext()!!.last() != ']')
                    list.add(parseAny(context) ?: return list.toArray())
                else
                    next()
            }
            return list.toArray()
        }
        return null
    }

    override fun except(except: String): Boolean {
        return if (peekNext() == except) {
            next();true;
        } else false
    }
}