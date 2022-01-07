package com.skillw.pouvoir.util

import org.bukkit.configuration.serialization.ConfigurationSerializable
import taboolib.library.configuration.ConfigurationSection

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

    @JvmStatic
    fun <T : ConfigurationSerializable> build(config: ConfigurationSection, key: String): T? {
        val obj = config[key] ?: return null
        return obj as T
    }

    fun <T : ConfigurationSerializable> ConfigurationSection.create(key: String): T? {
        return build(this, key)
    }

    @JvmStatic
    fun <T : ConfigurationSerializable> buildMultiple(config: ConfigurationSection): Set<T> {
        val set = HashSet<T>()
        for (key in config.getKeys(false)) {
            val obj = build<T>(config, key) ?: continue
            set.add(obj)
        }
        return set
    }
}