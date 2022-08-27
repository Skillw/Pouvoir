package com.skillw.pouvoir.api.function

import com.skillw.pouvoir.api.function.parser.Parser

fun interface IFunction<T> {
    fun execute(parser: Parser): T?
}