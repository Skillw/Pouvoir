package com.skillw.asahi.api

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.parser.infix.InfixParser
import com.skillw.asahi.api.member.parser.prefix.TopPrefixParser.Companion.tryLangParse
import com.skillw.asahi.api.member.parser.prefix.namespacing.PrefixCreator
import com.skillw.asahi.api.member.parser.prefix.namespacing.PrefixParser
import com.skillw.asahi.api.member.parser.prefix.type.TypeParser
import com.skillw.asahi.api.member.quest.LazyQuester
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.member.quest.VarBeanQuester
import com.skillw.asahi.api.script.AsahiScriptException
import com.skillw.asahi.util.cast
import com.skillw.asahi.util.castSafely

/**
 * @className ReaderExt
 *
 * @author Glom
 * @date 2023/1/13 20:40 Copyright 2023 user. All rights reserved.
 */

/**
 * 安全寻求下一个值
 *
 * @param R 类型
 * @return 结果
 */
@Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
inline fun <reified R> AsahiLexer.questSafely(): Quester<R?> {
    val token = next()
    var getter =
        tryLangParse(token)
            ?: if (AsahiManager.hasParser(R::class.java)) {
                AsahiManager.getParser(R::class.java)?.parseWith(this)
            } else quester { token.cast() }
    getter ?: error("Cannot quest $token")
    getter = if (getter !is VarBeanQuester) InfixParser.get().parseInfix(this, getter) else getter
    val index = currentIndex()
    return object : Quester<R?> {
        override fun AsahiContext.execute(): R? {
            return runCatching { getter.get().castSafely<R>() }.run {
                if (isSuccess) getOrThrow()
                else exceptionOrNull().let {
                    if (it is AsahiScriptException) throw it
                    else throw AsahiScriptException(info("Error occurred", index), it)
                }
            }
        }

        override fun toString(): String {
            return getter.toString()
        }
    }
}

/**
 * 直接通过指定类型解释器寻求值 (安全)
 *
 * @return Quester<R?>
 * @receiver AsahiLexer
 */
@Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
inline fun <reified R> AsahiLexer.questTypeSafely(): Quester<R?> {
    val token = next()
    var getter: Quester<Any?>? = AsahiManager.getParser(R::class.java)?.parseWith(this) as Quester<Any?>
    getter ?: error("Cannot quest $token")
    getter = if (getter !is VarBeanQuester) InfixParser.get().parseInfix(this, getter) else getter
    val index = currentIndex()
    return object : Quester<R?> {
        override fun AsahiContext.execute(): R? {
            return runCatching { getter.get().castSafely<R>() }.run {
                if (isSuccess) getOrThrow()
                else exceptionOrNull().let {
                    if (it is AsahiScriptException) throw it
                    else throw AsahiScriptException(info("Error occurred", index), it)
                }
            }
        }

        override fun toString(): String {
            return getter.toString()
        }
    }
}

/**
 * 直接通过指定类型解释器寻求值 (强制)
 *
 * @param R 类型
 * @return 结果
 */
@Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
inline fun <reified R> AsahiLexer.questType(): Quester<R> {
    return questTypeSafely<R>() as Quester<R>
}

/**
 * 强制寻求下一个值
 *
 * @param R 类型
 * @return 结果
 */
@Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
inline fun <reified R> AsahiLexer.quest(): Quester<R> {
    return questSafely<R>() as Quester<R>
}

/**
 * 创建对象寻求者
 *
 * @param quest 执行内容
 * @param R 类型
 * @return 对象寻求者
 * @receiver
 */
fun <R> quester(quest: AsahiContext.() -> R): Quester<R> {
    return Quester { quest() }
}

/**
 * 将对象寻求者转为其它类型的对象寻求者
 *
 * @param quest 原对象寻求者
 * @param T 原类型
 * @param R 返回类型
 * @return 返回类型的对象寻求者
 * @receiver
 */
fun <T, R> Quester<T>.quester(quest: AsahiContext.(T) -> R): Quester<R> {
    return Quester { quest(get()) }
}


/**
 * 懒人式对象寻求着
 *
 * @param quest
 * @param R
 * @return
 * @receiver
 */
fun <R> lazyQuester(quest: AsahiContext.() -> R): LazyQuester<R> {
    return LazyQuester { quest() }
}

/**
 * 创建前缀解释器
 *
 * @param parseFunc 解释内容
 * @param R 返回类型
 * @return 前缀解释器
 * @receiver
 */
fun <R> prefixParser(
    parseFunc: PrefixParser<R>.() -> Quester<R>,
): PrefixCreator<R> {
    return object : PrefixCreator<R> {
        override fun PrefixParser<R>.parse(): Quester<R> {
            return parseFunc()
        }
    }
}

/**
 * 创建类型解释器
 *
 * @param types 类型
 * @param parseFunc 解释内容
 * @param R 返回类型
 * @return 类型解释器
 * @receiver
 */
fun <R> typeParser(
    vararg types: Class<*>,
    parseFunc: AsahiLexer.() -> Quester<R>,
): TypeParser<R> {
    return object : TypeParser<R>(*types) {
        override fun AsahiLexer.parse() = parseFunc()
    }
}



