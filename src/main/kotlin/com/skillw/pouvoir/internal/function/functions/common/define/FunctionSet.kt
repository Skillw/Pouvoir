package com.skillw.pouvoir.internal.function.functions.common.define

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.IFunction
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser

@AutoRegister
object FunctionSet : PouFunction<Any>("set") {
    override fun execute(parser: Parser): Any? {
        with(parser) {
            val key = parseString()
            when {
                except("by") && except("lazy") -> {
                    except("to", "=");
                    val block = parseBlock()
                    return IFunction {
                        block.execute(it)
                            .also { result -> it.context[key] = result ?: error("parse Ant error") }
                    }.also { context[key] = it }
                }

                except("ifndef") -> if (context.containsKey(key)) return context[key]
            }
            except("to", "=");
            val value = parseAny()
            value?.let { context[key] = it } ?: context.remove(key)
            return value
        }
    }
}