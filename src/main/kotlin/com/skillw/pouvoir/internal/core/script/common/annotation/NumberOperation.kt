package com.skillw.pouvoir.internal.core.script.common.annotation

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.feature.operation.NumberOperation
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.internal.manager.PouConfig
import com.skillw.pouvoir.util.toArgs
import taboolib.common.platform.function.console
import taboolib.common5.Coerce
import taboolib.module.lang.sendLang

/**
 * NumberOperation 运算操作注解 运算操作键 (可选 若无则为函数名)
 *
 * @constructor NumberOperation Key(Optional)
 */
@AutoRegister
internal object NumberOperation : ScriptAnnotation("NumberOperation") {
    override fun handle(data: ScriptAnnotationData) {
        val script = data.script
        val args = data.args.toArgs()
        val function = data.function
        val key = if (args.isEmpty() || args[0] == "") function else args[0]
        object : NumberOperation(key) {
            override fun operate(a: Number, b: Number): Number {
                return Coerce.toDouble(
                    Pouvoir.scriptManager.invoke(
                        script, function, parameters = arrayOf(a, b)
                    )
                )
            }
        }.register()
        PouConfig.debug { console().sendLang("annotation-number-operation-register", key) }
        script.onDeleted("NumberOperation-$key") {
            PouConfig.debug { console().sendLang("annotation-number-operation-unregister", key) }
            Pouvoir.operationManager.remove(key)
        }
    }
}