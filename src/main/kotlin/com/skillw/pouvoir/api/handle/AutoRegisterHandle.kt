package com.skillw.pouvoir.api.handle

import com.skillw.pouvoir.api.annotation.AutoRegister

object AutoRegisterHandle {
    fun isAutoRegister(clazz: Class<*>): Boolean {
        return clazz.isAnnotationPresent(AutoRegister::class.java)
    }

    fun register(clazz: Class<*>) {
        if (!isAutoRegister(clazz)) return
        

    }
}