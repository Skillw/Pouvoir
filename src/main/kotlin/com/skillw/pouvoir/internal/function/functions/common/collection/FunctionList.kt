package com.skillw.pouvoir.internal.function.functions.common.collection

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser

/**
 * @className FunctionBlock
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionList : PouFunction<Any>(
    "list"
) {
    override fun execute(parser: Parser): Any? {
        with(parser) {
            val indexable = parseAny()
            when (parseString()) {
                "get" -> {
                    except("at")
                    val index = parseInt()
                    return when (indexable) {
                        is MutableList<*> -> (indexable as MutableList<Any>)[index]

                        is Array<*> -> (indexable as Array<Any>)[index]

                        else -> null
                    }
                }

                "set" -> {
                    val value = parseAny() ?: error("parse Any failed")
                    except("at")
                    val index = parseInt()
                    return when (indexable) {
                        is MutableList<*> -> (indexable as MutableList<Any>)[index] = value

                        is Array<*> -> (indexable as Array<Any>)[index] = value

                        else -> null
                    }
                }

                "remove" -> {
                    except("at")
                    val index = parseInt()
                    return when (indexable) {
                        is MutableList<*> -> indexable.removeAt(index)

                        else -> null
                    }
                }

                "size" -> {

                    return when (indexable) {
                        is List<*> -> indexable.size
                        is Array<*> -> indexable.size
                        else -> null
                    }
                }

                "add" -> {
                    val value = parseAny() ?: error("parse Any failed")

                    return when (indexable) {
                        is MutableList<*> -> (indexable as MutableList<Any>).add(value)
                        else -> null
                    }
                }

                "clear" -> {

                    return when (indexable) {
                        is MutableList<*> -> (indexable as MutableList<Any>).clear()
                        else -> null
                    }
                }

                "contains" -> {
                    val value = parseAny() ?: error("parse Any failed")

                    return when (indexable) {
                        is List<*> -> indexable.contains(value)
                        is Array<*> -> indexable.contains(value)
                        else -> null
                    }
                }

                "isEmpty" -> {

                    return when (indexable) {
                        is List<*> -> indexable.isEmpty()
                        is Array<*> -> indexable.isEmpty()
                        else -> null
                    }
                }

                "toArray" -> {

                    return when (indexable) {
                        is List<*> -> indexable.toTypedArray()
                        else -> null
                    }
                }

                "toString" -> {

                    return when (indexable) {
                        is List<*> -> indexable.toString()
                        is Array<*> -> indexable.toString()
                        else -> null
                    }
                }

                else -> {
                    error("unknown index action")
                }
            }
        }
    }
}