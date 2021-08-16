package com.skillw.pouvoir.util

object ClassUtils {
    @JvmStatic
    val staticClass: Class<*> by lazy {
        if (System.getProperty("java.version").contains("1.8."))
            Class.forName("jdk.internal.dynalink.beans.StaticClass")
        else
            Class.forName("jdk.dynalink.beans.StaticClass")
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
    fun <T> isObj(clazz: Class<T>): Boolean {
        val fields = clazz.fields
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