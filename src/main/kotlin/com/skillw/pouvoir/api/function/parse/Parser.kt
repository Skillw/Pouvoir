package com.skillw.pouvoir.api.function.parse

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.function.IFunction
import com.skillw.pouvoir.api.function.context.IContext
import com.skillw.pouvoir.api.function.reader.IReader
import com.skillw.pouvoir.util.ColorUtils.decolored
import taboolib.common5.Coerce
import taboolib.module.chat.uncolored
import java.util.*

data class Parser(val reader: IReader, val context: IContext) : IReader by reader, IContext by context {

    fun skipTill(from: String, till: String): Boolean {
        return skipTill(reader, from, till)
    }

    fun splitTill(from: String, to: String): String {
        return splitTill(reader, from, to) ?: error("split $from to $to failed")
    }

    fun parseAny(): Any? {
        return parseAny(context)
    }

    fun parseString(): String {
        return parse()
    }

    fun parseInt(): Int {
        return parse()
    }

    fun parseLong(): Long {
        return parse()
    }

    fun parseFloat(): Float {
        return parse()
    }

    fun parseDouble(): Double {
        return parse()
    }

    fun parseBoolean(): Boolean {
        return parse()
    }

    fun parseByte(): Byte {
        return parse()
    }

    fun parseShort(): Short {
        return parse()
    }

    fun parseArray(): Array<Any> {
        return parse()
    }

    fun parseList(): List<Any> {
        return parse()
    }

    companion object {
        /**
         * Parse once
         *
         * @param context
         * @return
         */
        @JvmStatic
        inline fun <reified T> Parser.parse(): T {
            return when (T::class) {
                String::class -> parseString(context) as? T?
                Int::class -> parseInt(context) as? T?
                Long::class -> parseLong(context) as? T?
                Float::class -> parseFloat(context) as? T?
                Double::class -> parseDouble(context) as? T?
                Boolean::class -> parseBoolean(context) as? T?
                Byte::class -> parseByte(context) as? T?
                Short::class -> parseShort(context) as? T?
                Array<Any>::class -> parseArray(context) as? T?
                List::class -> parseList(context) as? T?
                else -> parseAny(context) as? T?
            } ?: error(
                "parse ${T::class.simpleName} error"
            )
        }
    }

    fun error(string: String): Nothing {
        kotlin.error(
            "$string at ${reader.currentIndex()} : ${reader.current()} in\n ${reader.string}".uncolored()
                .decolored()
        )
    }

    fun parseAny(context: IContext): Any? {
        val token = next() ?: return null
        if (token == "true") return true
        if (token == "false") return false
        if (token == "pass") return ""
        if (token == "null") return null
        if ((token.first() == '\'')) {
            if (token.last() == '\'' && token.length != 1) return token.replace("\'", "")
            val builder = StringBuilder(token.replace("\'", ""))
            while (hasNext()) {
                if (next()?.last() != '\'')
                    builder.append(" " + current())
                else {
                    builder.append(" " + current().replace("\'", ""))
                    break
                }
            }
            return builder.toString().replace("\\_", " ")
        }
        if (token.startsWith("&")) {
            return context[token.replace(
                "&",
                ""
            )].let { if (it is IFunction<*>) it.execute(this) else return it }
        }
        val function = context[token].run { if (this is IFunction<*>) this else return token }
        return function.execute(this)
    }

    fun parseString(context: IContext): String? {
        return parseAny(context)?.toString()
    }

    fun parseInt(context: IContext): Int? {
        return Coerce.asInteger(parseAny(context)).run { if (isPresent) get() else null }
    }

    fun parseLong(context: IContext): Long? {
        return Coerce.asLong(parseAny(context)).run { if (isPresent) get() else null }
    }

    fun parseFloat(context: IContext): Float? {
        return Coerce.asFloat(parseAny(context)).run { if (isPresent) get() else null }
    }

    fun parseDouble(context: IContext): Double? {
        return Coerce.asDouble(parseAny(context)).run { if (isPresent) get() else null }
    }

    fun parseBoolean(context: IContext): Boolean? {
        return Coerce.asBoolean(parseAny(context)).run { if (isPresent) get() else null }
    }

    fun parseByte(context: IContext): Byte? {
        return Coerce.asByte(parseAny(context)).run { if (isPresent) get() else null }
    }

    fun parseShort(context: IContext): Short? {
        return Coerce.asShort(parseAny(context)).run { if (isPresent) get() else null }
    }

    fun parseArray(context: IContext): Array<Any>? {
        return parseList(context)?.toTypedArray()
    }

    fun parseList(context: IContext): List<Any>? {
        val token = next() ?: return null
        if (token == "null") return null
        if ((token.first() == '[')) {
            with(reader) {
                val list = LinkedList<Any>()
                while (hasNext()) {
                    except(",")
                    list.add(parse())
                    if (except("]")) break
                }
                return list
            }
        }
        return context[token.replace("&", "")] as? List<Any>?
    }

    fun except(vararg excepts: String): Boolean {
        return excepts.any {
            (peekNext() == it).also { bool ->
                if (bool) {
                    next();return@any true
                }
            }
        }
    }

    fun parseBlock(): IFunction<Any> {
        val process = splitTill("{", "}")
        return IFunction { Pouvoir.pouFunctionManager.parse(process, context = context) }
    }
}

