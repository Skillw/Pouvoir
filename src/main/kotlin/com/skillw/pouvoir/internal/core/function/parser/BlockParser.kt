package com.skillw.pouvoir.internal.core.function.parser

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.IBlock
import com.skillw.pouvoir.api.function.context.IContext
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.api.function.parser.TokenParser

@AutoRegister
object BlockParser : TokenParser(IBlock::class.java) {
    override fun parse(parser: Parser): Any {
        with(parser) {
            val process = splitTill("{", "}")
            return object : IBlock<Any?> {
                override fun execute(context: IContext): Any? {
                    return Pouvoir.pouFunctionManager.eval(process, context = context)
                }

                override fun execute(parser: Parser): Any? {
                    return execute(parser.context)
                }

                override fun toString(): String {
                    return process
                }
            }
        }
    }
}