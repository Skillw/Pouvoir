package com.skillw.pouvoir.internal.handle

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.script.engine.PouScriptEngine

object PouEngineHandle {
    private fun isPouEngineClass(clazz: Class<*>): Boolean =
        PouScriptEngine::class.java.isAssignableFrom(clazz) && clazz != PouScriptEngine::class.java

    fun inject(clazz: Class<*>) {
        try {
            if (!isPouEngineClass(clazz) || !clazz.isAnnotationPresent(AutoRegister::class.java)) return
            val pouScriptEngine = clazz.getField("INSTANCE").get(null) as PouScriptEngine
            if (Pouvoir.scriptEngineManager.containsKey(pouScriptEngine.key)) return
            pouScriptEngine.register()
        } catch (e: Exception) {

        }
    }
}