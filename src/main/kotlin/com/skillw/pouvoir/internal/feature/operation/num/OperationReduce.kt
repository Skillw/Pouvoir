package com.skillw.pouvoir.internal.feature.operation.num

import com.skillw.pouvoir.api.feature.operation.NumberOperation
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister

@AutoRegister
object OperationReduce : NumberOperation("reduce", "-") {
    override fun operate(a: Number, b: Number): Number {
        return a.toDouble() - b.toDouble()
    }
}
