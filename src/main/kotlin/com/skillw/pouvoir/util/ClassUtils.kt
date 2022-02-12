package com.skillw.pouvoir.util

@Suppress("UNCHECKED_CAST")
object ClassUtils {
    @JvmStatic
    fun getClass(path: String): Class<*>? {
        val clazz: Class<*>
        try {
            clazz = Class.forName(path)
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