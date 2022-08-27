package com.skillw.pouvoir.internal.core.function.functions.common.define

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.IFunction
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.internal.core.function.functions.common.define.FunctionSet.ILazy

@AutoRegister
object FunctionSet : PouFunction<Any>("set") {

    fun interface ILazy : IFunction<Any>

    override fun execute(parser: Parser): Any? {
        with(parser) {
            val key = parseString()
            when {
                except("ifndef") -> if (context.containsKey(key)) return context[key]

                except("by") && except("lazy") -> {
                    except("to", "=");

                    val block = parseBlock()
                    return ILazy {
                        block.execute(it)
                            .also { result -> it.context[key] = result ?: error("parse Any error") }
                    }.also { context[key] = it }
                }
            }
            except("to", "=");
            val value = parseNext<Any>()
            value?.let { context[key] = it } ?: context.remove(key)
            return value
        }
    }
}