package com.skillw.pouvoir.util

import taboolib.library.reflex.ReflexClass

@Suppress("UNCHECKED_CAST")
object ClassUtils {
    @JvmStatic
    fun String.findClass(): Class<*>? {
        val clazz: Class<*>
        try {
            clazz = Class.forName(this)
        } catch (e: Exception) {
            MessageUtils.wrong("The class $this dose not exist!")
            return null
        }
        return clazz
    }


    @JvmStatic
    val staticClass: Class<*> by lazy(LazyThreadSafetyMode.NONE) {
        try {
            Class.forName("jdk.internal.dynalink.beans.StaticClass")
        } catch (throwable: Throwable) {
            Class.forName("jdk.dynalink.beans.StaticClass")
        }

    }

    @JvmStatic
    fun staticClass(className: String): Any? {
        return ReflexClass.of(staticClass).getMethod("forClass").invokeStatic(className.findClass())
    }
}