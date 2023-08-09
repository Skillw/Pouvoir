package com.skillw.pouvoir.util

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.plugin.TotalManager
import taboolib.common.platform.function.console
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.module.lang.sendLang
import java.lang.reflect.Method

/**
 * Class相关工具类
 *
 * @constructor Create empty Class utils
 */

private fun String.relocate() = Pouvoir.scriptEngineManager.relocatePath(this)


val Class<*>.instance: Any?
    get() = kotlin.runCatching {
        getDeclaredField("INSTANCE").get(null) ?: getField("instance").get(null) ?: getConstructor().newInstance()
    }.getOrNull()


fun String.existClass(): Boolean {
    return kotlin.runCatching { Class.forName(relocate()) }.isSuccess
}


fun String.findClass(): Class<*>? {
    var clazz: Class<*>? = null
    val path = relocate()
    kotlin.runCatching {
        clazz = Class.forName(path)
    }.exceptionOrNull()?.run {
        console().sendLang("class-not-found", path)
    }
    return clazz
}


fun find(name: String): Any? {
    return name.findClass()?.static()
}


val getStaticClass: Method by lazy(LazyThreadSafetyMode.NONE) {
    try {
        Class.forName("!jdk.internal.dynalink.beans.StaticClass".substring(1))
    } catch (throwable: Throwable) {
        Class.forName("jdk.dynalink.beans.StaticClass")
    }.getMethod("forClass", Class::class.java)

}


fun staticClass(className: String): Any? {
    return if (!className.contains("."))
        return TotalManager.allStaticClasses[className]
    else className.findClass()?.static()
}


fun Class<*>.static(): Any {
    return getStaticClass.invoke(null, this)!!
}


fun Any.isStaticClass(): Boolean {
    return javaClass.simpleName == "StaticClass"
}


fun Any.instanceof(staticClass: Any): Boolean {
    return !staticClass.isStaticClass() || staticClass.getProperty<Class<*>>("representedClass")!!.isInstance(this)
}
