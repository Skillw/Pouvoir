package com.skillw.pouvoir.api.map

import com.skillw.pouvoir.api.able.Keyable
import taboolib.common.reflect.ReflexClass
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException

/**
 * ClassName : com.skillw.classsystem.api.map.ClazzMap
 * Created by Glom_ on 2021-03-26 21:47:29
 * Copyright  2021 user. All rights reserved.
 */
open class ClazzMap<T : Keyable<String>> : BaseMap<String, Class<out T>>() {
    private fun getId(clazz: Class<out T>): String? {
        try {
            return clazz.getField("key").get(null).toString()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        }
        return null
    }

    fun register(clazz: Class<out T>) {
        map[getId(clazz)!!] = clazz
    }

    fun removeByValue(clazz: Class<out T>) {
        map.remove(getId(clazz))
    }

    override fun hasObject(value: Class<out T>): Boolean {
        return map.containsKey(getId(value))
    }

    @Suppress("UNCHECKED_CAST")
    fun getObject(key: String, vararg params: Any): T? {
        if (!map.containsKey(key)) {
            return null
        }
        try {
            val constructor: Constructor<*> = ReflexClass(map[key]!!).findConstructor(*params) ?: return null
            return constructor.newInstance(*params) as T
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return null
    }
}