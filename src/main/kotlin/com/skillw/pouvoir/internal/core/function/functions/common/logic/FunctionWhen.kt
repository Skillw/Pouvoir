package com.skillw.pouvoir.internal.core.function.functions.common.logic

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.internal.core.function.reader.SimpleReader

/**
 * @className FunctionAll
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionWhen : PouFunction<Any>(
    "when", "switch"
) {
    override fun execute(parser: Parser): Any? {
        with(parser) {
            val value = if (except("of")) parseAny() else null
            val phrase = splitTill("{", "}")
            val innerParser = Parser.createParser(SimpleReader.create(phrase), context)
            val outerError = this::error
            with(innerParser) {
                fun case(): Any? {
                    if (peek() == "{") skipTill("{", "}")
                    if (!hasNext()) return null
                    when {
                        except("case", "when") -> {
                            val bool =
                                value?.let {
                                    FunctionCheck.check(value, parseString(), parseAny())
                                } ?: parseBoolean()
                            except("to", "->")
                            return if (bool) {
                                (if (peek() == "{") parseBlock().execute(context) else parseAny())
                                    ?: error("The return value cannot be null!")
                            } else {
                                if (peek() == "{") parseBlock() else parseAny()
                                case()
                            }
                        }

                        except("else", "default") -> {
                            return parseBlock().execute(context)
                        }

                        else -> error("Wrong 'when' format in the { }")
                    }
                }
                return case()
            }
        }
    }
}