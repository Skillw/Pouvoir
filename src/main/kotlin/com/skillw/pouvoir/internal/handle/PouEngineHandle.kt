package com.skillw.pouvoir.internal.handle

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.script.engine.PouScriptEngine

object PouEngineHandle {
    private fun isPouEngineClass(clazz: Class<*>): Boolean =
        PouScriptEngine::class.java.isAssignableFrom(clazz) && clazz != PouScriptEngine::class.java

    fun inject(clazz: Class<*>) {
        try {
            if (!isPouEngineClass(clazz)) return
            val pouScriptEngine = clazz.getField("INSTANCE").get(null) as PouScriptEngine
            if (Pouvoir.scriptEngineManager.containsKey(pouScriptEngine.key))
                return
            pouScriptEngine.register()
        } catch (e: Exception) {
            
        }
    }
}