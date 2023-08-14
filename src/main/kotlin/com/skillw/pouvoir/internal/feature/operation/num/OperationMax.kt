package com.skillw.pouvoir.internal.feature.operation.num

import com.skillw.pouvoir.api.feature.operation.NumberOperation
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import kotlin.math.max

@AutoRegister
object OperationMax : NumberOperation("max", "<") {
    override fun operate(a: Number, b: Number): Number {
        return max(a.toDouble(), b.toDouble())
    }
}
