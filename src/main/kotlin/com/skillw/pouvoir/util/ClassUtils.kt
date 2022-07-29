package com.skillw.pouvoir.util

import com.skillw.pouvoir.api.annotation.ScriptTopLevel
import taboolib.library.reflex.ReflexClass

@Suppress("UNCHECKED_CAST")
object ClassUtils {
    @JvmStatic
    fun String.findClass(): Class<*>? {
        val clazz: Class<*>
        try {
            clazz = Class.forName(this)
        } catch (e: Exception) {
            MessageUtils.warning("The class $this dose not exist!")
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
        return ReflexClass.of(staticClass).structure.getMethod("forClass", this).invokeStatic(this)!!
    }

    @JvmStatic
    fun Any.isStaticClass(): Boolean {
        return javaClass.simpleName == "StaticClass"
    }
}