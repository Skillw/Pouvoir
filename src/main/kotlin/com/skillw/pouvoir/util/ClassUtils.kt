package com.skillw.pouvoir.util

import com.skillw.pouvoir.api.plugin.TotalManager
import com.skillw.pouvoir.internal.manager.ScriptEngineManagerImpl.relocates
import com.skillw.pouvoir.util.StringUtils.replacement
import ink.ptms.adyeshach.taboolib.common.reflect.Reflex.Companion.getProperty
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import java.lang.reflect.Method

@Suppress("UNCHECKED_CAST")
object ClassUtils {
    @JvmStatic
    fun String.existClass(): Boolean {
        return try {
            Class.forName(replacement(relocates))
            true
        } catch (e: Exception) {
            false
        }
    }

    @JvmStatic
    fun String.findClass(): Class<*>? {
        val clazz: Class<*>
        val path = replacement(relocates)
        try {
            clazz = Class.forName(path)
        } catch (e: Exception) {
            console().sendLang("class-not-found", path)
            return null
        }
        return clazz
    }

    @JvmStatic
    fun find(name: String): Any? {
        return name.findClass()?.static()
    }

    @JvmStatic
    val getStaticClass: Method by lazy(LazyThreadSafetyMode.NONE) {
        try {
            Class.forName("jdk.internal.dynalink.beans.StaticClass")
        } catch (throwable: Throwable) {
            Class.forName("jdk.dynalink.beans.StaticClass")
        }.getMethod("forClass", Class::class.java)

    }


    @JvmStatic
    fun staticClass(className: String): Any? {
        return if (!className.contains("."))
            return TotalManager.allStaticClasses[className]
        else className.findClass()?.static()
    }

    @JvmStatic
    fun Class<*>.static(): Any {
        return getStaticClass.invoke(null, this)!!
    }

    @JvmStatic
    fun Any.isStaticClass(): Boolean {
        return javaClass.simpleName == "StaticClass"
    }

    @JvmStatic
    fun Any.instanceof(staticClass: Any): Boolean {
        return !staticClass.isStaticClass() || staticClass.getProperty<Class<*>>("representedClass")!!.isInstance(this)
    }
}