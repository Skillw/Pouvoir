package com.skillw.pouvoir.util

import com.skillw.pouvoir.api.annotation.ScriptTopLevel
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

@Suppress("UNCHECKED_CAST")
object ClassUtils {
    @JvmStatic
    fun String.findClass(): Class<*>? {
        val clazz: Class<*>
        try {
            clazz = Class.forName(this)
        } catch (e: Exception) {
            console().sendLang("class-not-found", this)
            return null
        }
        return clazz
    }

    @ScriptTopLevel
    @JvmStatic
    fun find(name: String): Any? {
        if (name.contains(">taboolib.")) {
            return "com.skillw.pouvoir.${name.replace(">", "")}".findClass()?.static()
        }
        return name.findClass()?.static()
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
        return className.findClass()?.static()
    }

    @JvmStatic
    fun Class<*>.static(): Any {
        return staticClass.getMethod("forClass", Class::class.java).invoke(null, this)!!
    }

    @JvmStatic
    fun Any.isStaticClass(): Boolean {
        return javaClass.simpleName == "StaticClass"
    }
}