package com.skillw.pouvoir.api.map

import com.skillw.pouvoir.api.able.Keyable
import com.skillw.rpglib.api.map.BaseMap
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException

/**
 * ClassName : com.skillw.classsystem.api.map.ClazzMap
 * Created by Glom_ on 2021-03-26 21:47:29
 * Copyright  2021 user. All rights reserved.
 */
abstract class ClazzMap<T : Keyable<String>> : BaseMap<String, Class<T>>() {
    fun getId(clazz: Class<T>): String? {
        try {
            return clazz.newInstance().key
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        }
        return null
    }

    fun register(clazz: Class<T>) {
        map[getId(clazz)!!] = clazz
    }

    fun removeByValue(clazz: Class<T>) {
        map.remove(getId(clazz))
    }

    override fun hasObject(clazz: Class<T>): Boolean {
        return map.containsKey(getId(clazz))
    }

    fun getObject(key: String, vararg params: Any): T? {
        if (!map.containsKey(key)) {
            return null
        }
        try {
            var classes: Array<Class<*>?>? = null
            if (params.size != 0) {
                classes = arrayOfNulls<Class<*>?>(params.size)
                for (i in 0 until params.size) {
                    classes[i] = params[i].javaClass
                }
            }
            val constructor: Constructor<T> = if (classes == null) map[key]!!
                .getConstructor() else map[key]!!.getConstructor(*classes)
            return constructor.newInstance(*params)
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

    fun getObject(key: String): T? {
        try {
            val constructor: Constructor<T> = map[key]!!.getConstructor()
            return constructor.newInstance()
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