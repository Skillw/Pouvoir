package com.skillw.pouvoir.api.function.parser

import com.skillw.pouvoir.api.able.Registrable

/**
 * @className TokenParser
 *
 * @author Glom
 * @date 2022/8/17 10:24 Copyright 2022 user. All rights reserved.
 */
abstract class TokenParser(override val key: Class<*>, vararg val aliases: Class<*>) : Registrable<Class<*>> {

    abstract fun parse(parser: Parser): Any?

    override fun register() {
        Parser.register(this)
    }
}