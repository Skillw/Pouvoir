package com.skillw.asahi.api.member.context

import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.quest.LazyQuester
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.questSafely
import com.skillw.asahi.internal.util.Time

/**
 * @className InfixContext
 *
 * @author Glom
 * @date 2022/12/24 15:05 Copyright 2022 user. All rights reserved.
 */
open class InfixContext(
    val context: AsahiContext, val reader: AsahiLexer, var token: String = "@NONE",
) : AsahiContext by context, AsahiLexer by reader {
    inline fun <reified R> parse(): R = quest<R>().get()
    fun parseString() = parse<String>()

    /** 解析 int */
    fun parseInt() = parse<Int>()

    /** 解析 double */
    fun parseDouble() = parse<Double>()

    /** 解析 float */
    fun parseFloat() = parse<Float>()

    /** 解析 byte */
    fun parseByte() = parse<Byte>()

    /** 解析 short */
    fun parseShort() = parse<Short>()

    /** 解析 boolean */
    fun parseBoolean() = parse<Boolean>()

    /** 解析 long */
    fun parseLong() = parse<Long>()

    /** 解析 list */
    fun parseList() = parse<MutableList<Any?>>()

    /** 解析 array */
    fun parseArray() = parse<Array<Any?>>()

    /** 解析 map */
    fun parseMap() = parse<MutableMap<String, Any?>>()

    /** 解析 tokenizer */
    fun parseObj() = questSafely<Any?>().get()
    fun parseAny() = parse<Any>()

    /** 解析 map */
    fun parseLazy() = parse<LazyQuester<Any?>>()

    /** 解析 Time */
    fun parseTime() = parse<Time>()

    /** 解析 Tick */
    fun parseTick() = questTick().get()

    override fun debugOn() {
        reader.debugOn()
        context.debugOn()
    }

    override fun debugOff() {
        reader.debugOff()
        context.debugOff()
    }

    override fun reset() {
        reader.reset()
        context.reset()
    }


    override fun clone(): InfixContext {
        return InfixContext(context().clone(), this, token)
    }
}