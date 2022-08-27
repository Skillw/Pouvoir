package com.skillw.pouvoir.internal.core.function.functions.common.define

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.IBlock
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser

@AutoRegister
object FunctionBlock : PouFunction<IBlock<Any?>>("block") {
    override fun execute(parser: Parser): IBlock<Any?> {
        with(parser) {
            return parseBlock()
        }
    }
}