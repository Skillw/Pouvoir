package com.skillw.pouvoir.internal.handle

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.function.PouFunction

object PouFunctionHandle {
    private fun isPouFunction(clazz: Class<*>): Boolean =
        PouFunction::class.java.isAssignableFrom(clazz) && clazz != PouFunction::class.java

    fun inject(clazz: Class<*>) {
        try {
            if (!isPouFunction(clazz)) return
            val pouFunction = clazz.getField("INSTANCE").get(null) as PouFunction
            if (Pouvoir.functionManager.containsKey(pouFunction.key))
                return
            pouFunction.register()
        } catch (e: Exception) {
            
        }
    }
}