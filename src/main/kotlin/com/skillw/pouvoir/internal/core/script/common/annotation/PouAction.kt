package com.skillw.pouvoir.internal.core.script.common.annotation

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.PouAction
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.internal.manager.PouConfig
import com.skillw.pouvoir.util.ClassUtils.staticClass
import com.skillw.pouvoir.util.StringUtils.toArgs
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

/**
 * Inline Function Action
 *
 * @constructor Inline Function Action
 */
@AutoRegister
internal object PouAction : ScriptAnnotation("PouAction") {
    override fun handle(data: ScriptAnnotationData) {
        val script = data.script
        val args = data.args.toArgs()
        val function = data.function
        if (args.isEmpty()) error("At least 2 arguments: key , typeClass")
        val key = if (args.size == 1) function else args[0]
        val type: Class<out Any> = staticClass(args[1]) as? Class<out Any> ?: return
        Pouvoir.pouActionManager.getOrPut(type) { PouAction.createAction(type) {} }.apply {
            addExec(key) {
                return@addExec Pouvoir.scriptManager.invoke<Any?>(script, function, parameters = arrayOf(this, it))
            }
        }
        PouConfig.debug { console().sendLang("annotation-action-register", key) }
        script.onDeleted("Inline-Function-Action-$key") {
            PouConfig.debug { console().sendLang("annotation-action-unregister", key) }
            Pouvoir.pouActionManager[type]?.removeExec(key)
        }
    }
}