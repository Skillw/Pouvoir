package com.skillw.pouvoir.internal.feature.operation.str

import com.skillw.pouvoir.api.feature.operation.StringOperation
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.internal.manager.PouConfig
import taboolib.common.util.join

@AutoRegister
object OperationAppend : StringOperation("append") {
    override fun operate(a: String, b: String): String {
        return join(arrayOf(a, b), separator = PouConfig.strAppendSeparator)
    }

}