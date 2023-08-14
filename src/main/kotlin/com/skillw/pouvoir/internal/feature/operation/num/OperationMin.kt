package com.skillw.pouvoir.internal.feature.operation.num

import com.skillw.pouvoir.api.feature.operation.NumberOperation
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import kotlin.math.min

@AutoRegister
object OperationMin : NumberOperation("min", ">") {
    override fun operate(a: Number, b: Number): Number {
        return min(a.toDouble(), b.toDouble())
    }
}
