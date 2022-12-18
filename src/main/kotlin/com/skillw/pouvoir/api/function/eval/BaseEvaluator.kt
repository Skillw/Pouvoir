package com.skillw.pouvoir.api.function.eval

import com.skillw.pouvoir.Pouvoir.pouFunctionManager
import com.skillw.pouvoir.api.function.context.IContext
import com.skillw.pouvoir.api.function.reader.IReader

/**
 * @className Evaluator
 *
 * @author Glom
 * @date 2022/10/24 13:26 Copyright 2022 user. All rights reserved.
 */
abstract class BaseEvaluator(val namespaces: Array<String> = arrayOf(""), val baseContext: IContext) {
    init {
        baseContext.apply {
            namespaces.forEach {
                pouFunctionManager.namespaces[it]?.forEach { function ->
                    functions[function.key] = function
                }
            }
        }
    }

    abstract fun evaluate(script: String): Any?
    abstract fun evaluate(reader: IReader): Any?
}