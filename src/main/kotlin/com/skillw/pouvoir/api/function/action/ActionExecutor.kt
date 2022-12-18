package com.skillw.pouvoir.api.function.action

import com.skillw.pouvoir.api.function.parser.Parser

/**
 * @className ActionExecutor
 *
 * @author Glom
 * @date 2022/12/14 8:36 Copyright 2022 user. All rights reserved.
 */
fun interface ActionExecutor<T> {
    fun Parser.executor(obj: T): Any?
}