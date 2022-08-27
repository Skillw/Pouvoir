package com.skillw.pouvoir.api.function.parser

import com.skillw.pouvoir.api.function.IBlock
import com.skillw.pouvoir.api.function.action.IAction.Companion.action
import com.skillw.pouvoir.api.function.context.IContext
import com.skillw.pouvoir.api.function.reader.IReader
import com.skillw.pouvoir.internal.core.function.parser.FunctionParser
import com.skillw.pouvoir.internal.core.function.parser.StringParser
import com.skillw.pouvoir.internal.core.function.parser.VarParser
import com.skillw.pouvoir.util.ColorUtils.decolored
import taboolib.common.util.nonPrimitive
import taboolib.module.chat.uncolored

/**
 * Parser
 *
 * @constructor Create empty Parser
 * @property reader
 * @property context
 */
class Parser private constructor(val reader: IReader, val context: IContext) : IReader by reader, IContext by context {

    /**
     * Parse any
     *
     * @param T
     * @return
     */
    inline fun <reified T> parseNext(): T? {
        val token = peekNext()
        if (token == null || token == "null") kotlin.run { next();return null }
        val subParser = when {
            token.startsWith("&") -> VarParser
            functions.containsKey(token) -> FunctionParser
            else -> T::class.java.getParser()
        }
        return action(subParser.parse(this)) as? T?
    }

    /**
     * Parse not null
     *
     * @param T
     * @return
     */
    inline fun <reified T> parse(): T {
        return parseNext<T>() ?: error("parse ${T::class.simpleName} error")
    }


    /**
     * Parse any
     *
     * @return
     */
    fun parseAny(): Any {
        return parse()
    }

    /**
     * Parse string
     *
     * @return
     */
    fun parseString(): String {
        return parse()
    }

    /**
     * Parse int
     *
     * @return
     */
    fun parseInt(): Int {
        return parse()
    }

    /**
     * Parse long
     *
     * @return
     */
    fun parseLong(): Long {
        return parse()
    }

    /**
     * Parse float
     *
     * @return
     */
    fun parseFloat(): Float {
        return parse()
    }

    /**
     * Parse double
     *
     * @return
     */
    fun parseDouble(): Double {
        return parse()
    }

    /**
     * Parse boolean
     *
     * @return
     */
    fun parseBoolean(): Boolean {
        return parse()
    }

    /**
     * Parse byte
     *
     * @return
     */
    fun parseByte(): Byte {
        return parse()
    }

    /**
     * Parse short
     *
     * @return
     */
    fun parseShort(): Short {
        return parse()
    }

    /**
     * Parse array
     *
     * @return
     */
    fun parseArray(): Array<Any> {
        return parse()
    }

    /**
     * Parse list
     *
     * @return
     */
    fun parseList(): MutableList<Any> {
        return parse()
    }

    /**
     * Parse map
     *
     * @return
     */
    fun parseMap(): MutableMap<String, Any> {
        return parse()
    }

    /**
     * Parse block
     *
     * @return
     */
    fun parseBlock(): IBlock<Any?> {
        return parse()
    }

    /**
     * Except
     *
     * @param excepts
     * @return
     */
    fun except(vararg excepts: String): Boolean {
        return excepts.any {
            (peekNext() == it).also { bool ->
                if (bool) {
                    next();return@any true
                }
            }
        }
    }

    /**
     * Skip till
     *
     * @param from
     * @param till
     * @return
     */
    fun skipTill(from: String, till: String): Boolean {
        return skipTill(reader, from, till)
    }

    /**
     * Split till
     *
     * @param from
     * @param to
     * @return
     */
    fun splitTill(from: String, to: String): String {
        return splitTill(reader, from, to) ?: error("split $from to $to failed")
    }

    /**
     * Error
     *
     * @param string
     * @return
     */
    fun error(string: String): Nothing {
        kotlin.error(
            "$string at ${reader.currentIndex()} : ${reader.current()} in\n ${reader.string}".uncolored()
                .decolored()
        )
    }

    companion object {
        @JvmStatic
        private val registry = HashMap<Class<*>, TokenParser>()

        fun Class<*>.getParser(): TokenParser {
            return registry[this] ?: registry[registry.keys.firstOrNull { it.isAssignableFrom(this) }] ?: StringParser
        }

        fun register(parser: TokenParser) {
            registry[parser.key.nonPrimitive()] = parser
            parser.aliases.forEach {
                registry[it.nonPrimitive()] = parser
            }
        }

        @JvmStatic
        fun createParser(reader: IReader, context: IContext): Parser {
            return Parser(reader, context)
        }
    }
}