package com.skillw.pouvoir.internal.annotation

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.internal.function.PouScriptFunction
import com.skillw.pouvoir.internal.manager.PouConfig
import com.skillw.pouvoir.util.StringUtils.toArgs
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

/**
 * Inline Function
 *
 * @constructor Inline Function Key(Optional)
 */
@AutoRegister
object Function : ScriptAnnotation("Function") {
    override fun handle(data: ScriptAnnotationData) {
        val script = data.script
        val args = data.args.toArgs()
        val function = data.function
        val key = if (args.isEmpty() || args[0] == "") function else args[0]
        PouScriptFunction(key, "${script.key}::$function").register()
        PouConfig.debug { console().sendLang("annotation-function-register", key) }
        script.onDeleted("Inline-Function-$key") {
            PouConfig.debug { console().sendLang("annotation-function-unregister", key) }
            Pouvoir.inlineFunctionManager.remove(key)
        }
    }
}