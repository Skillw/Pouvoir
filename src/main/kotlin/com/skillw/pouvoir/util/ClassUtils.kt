package com.skillw.pouvoir.util

import org.bukkit.event.Event

@Suppress("UNCHECKED_CAST")
object ClassUtils {
    @JvmStatic
    fun getEventClass(path: String): Class<out Event>? {
        val clazz: Class<out Event>
        try {
            val tempClazz = Class.forName(path)
            if (!Event::class.java.isAssignableFrom(tempClazz)) {
                MessageUtils.wrong("The class $path is not a Event")
                return null
            }
            clazz = tempClazz.asSubclass(Event::class.java)
        } catch (e: Exception) {
            MessageUtils.wrong("The class $path dose not exist!")
            return null
        }
        return clazz
    }

    @JvmStatic
    val staticClass: Class<*> by lazy {
        try {
            Class.forName("jdk.internal.dynalink.beans.StaticClass")
        } catch (throwable: Throwable) {
            Class.forName("jdk.dynalink.beans.StaticClass")
        }

    }

    @JvmStatic
    fun staticClass(className: String): Any? {
        return try {
            staticClass.getMethod("forClass", Class::class.java).invoke(null, Class.forName(className))
        } catch (e: Exception) {
            null
        }
    }

    @JvmStatic
    fun <T> Class<T>.isObj(): Boolean {
        val fields = this.fields
        var isObj = false
        for (field in fields) {
            if (field.name == "INSTANCE") {
                isObj = true
                break
            }
        }
        return isObj
    }
}