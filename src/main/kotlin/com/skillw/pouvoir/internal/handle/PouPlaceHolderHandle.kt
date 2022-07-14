package com.skillw.pouvoir.internal.handle

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.placeholder.PouPlaceHolder

object PouPlaceHolderHandle {
    private fun isPouPlaceHolder(clazz: Class<*>): Boolean =
        PouPlaceHolder::class.java.isAssignableFrom(clazz) && clazz != PouPlaceHolder::class.java

    fun inject(clazz: Class<*>) {
        try {
            if (!isPouPlaceHolder(clazz) || !clazz.isAnnotationPresent(AutoRegister::class.java)) return
            val pouPlaceHolder = clazz.getField("INSTANCE").get(null) as PouPlaceHolder
            if (Pouvoir.pouPlaceholderManager.containsKey(pouPlaceHolder.key)) return
            pouPlaceHolder.register()
        } catch (e: Exception) {

        }
    }
}