package com.skillw.pouvoir.internal.core.script.common.annotation

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.internal.manager.PouConfig.debug
import com.skillw.pouvoir.util.StringUtils.toArgs
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

/**
 * Annotation
 *
 * @constructor Annotation Key(Optional)
 */
@AutoRegister
internal object Annotation : ScriptAnnotation("Annotation") {
    override fun handle(data: ScriptAnnotationData) {
        val script = data.script
        val args = data.args.toArgs()
        val function = data.function
        val key = if (args.isEmpty() || args[0] == "") function else args[0]
        object : ScriptAnnotation(key) {
            override fun handle(data: ScriptAnnotationData) {
                Pouvoir.scriptManager.invoke<Unit>(script, function, parameters = arrayOf(data))
            }
        }.register()
        debug { console().sendLang("annotation-annotation-register", key) }
        script.onDeleted("Script-Annotation-$key") {
            debug { console().sendLang("annotation-annotation-unregister", key) }
            Pouvoir.scriptAnnotationManager.remove(key)
        }
    }
}