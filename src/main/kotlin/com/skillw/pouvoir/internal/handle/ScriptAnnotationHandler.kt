package com.skillw.pouvoir.internal.handle

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation

object ScriptAnnotationHandler {
    private fun isScriptAnnotation(clazz: Class<*>): Boolean =
        ScriptAnnotation::class.java.isAssignableFrom(clazz) && clazz != ScriptAnnotation::class.java

    fun inject(clazz: Class<*>) {
        try {
            if (!isScriptAnnotation(clazz)) return
            val scriptAnnotation = clazz.getField("INSTANCE").get(null) as ScriptAnnotation
            if (Pouvoir.scriptAnnotationManager.containsKey(scriptAnnotation.key))
                return
            scriptAnnotation.register()
        } catch (e: Exception) {

        }
    }
}