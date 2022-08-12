package com.skillw.pouvoir.api.function

import com.skillw.pouvoir.api.function.parse.Parser

fun interface IFunction<T> {
    fun execute(parser: Parser): T?
}