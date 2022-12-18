package com.skillw.pouvoir.internal.core.function.eval

import com.skillw.pouvoir.api.function.context.IContext
import com.skillw.pouvoir.api.function.eval.BaseEvaluator
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.api.function.reader.IReader
import com.skillw.pouvoir.internal.core.function.context.SimpleContext
import com.skillw.pouvoir.internal.core.function.reader.SimpleReader

/**
 * @className Evaluator
 *
 * @author Glom
 * @date 2022/10/24 13:26 Copyright 2022 user. All rights reserved.
 */
class SimpleEvaluator(namespaces: Array<String> = arrayOf(), baseContext: IContext = SimpleContext()) :
    BaseEvaluator(namespaces, baseContext) {

    constructor(namespaces: Array<String> = arrayOf(), builder: IContext.() -> Unit) : this(
        namespaces,
        SimpleContext().apply(builder)
    )

    override fun evaluate(script: String): Any? {
        return evaluate(SimpleReader.create(script))
    }

    override fun evaluate(reader: IReader): Any? {
        val parser = Parser.createParser(reader, baseContext)
        with(parser) {
            kotlin.runCatching<Any?> {
                while (hasNext()) {
                    parseNext<Any?>()?.also {
                        if (!hasNext()) return it
                    } ?: break
                }
                return null
            }.getOrElse { it.printStackTrace(); error("Error occurred!"); }
        }
        return null
    }
}