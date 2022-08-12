package com.skillw.pouvoir.internal.annotation

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
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
        object : PouFunction<Any>(key) {
            override fun execute(parser: Parser): Any? {
                return Pouvoir.scriptManager.invoke(script, function, parameters = arrayOf(parser))
            }
        }.register()
        PouConfig.debug { console().sendLang("annotation-function-register", key) }
        script.onDeleted("Inline-Function-$key") {
            PouConfig.debug { console().sendLang("annotation-function-unregister", key) }
            Pouvoir.pouFunctionManager.remove(key)
        }
    }
}