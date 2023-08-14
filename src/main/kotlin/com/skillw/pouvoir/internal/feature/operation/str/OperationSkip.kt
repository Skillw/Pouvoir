package com.skillw.pouvoir.internal.feature.operation.str

import com.skillw.pouvoir.api.feature.operation.StringOperation
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister

@AutoRegister
object OperationSkip : StringOperation("skip") {
    override fun operate(a: String, b: String): String {
        return a
    }
}