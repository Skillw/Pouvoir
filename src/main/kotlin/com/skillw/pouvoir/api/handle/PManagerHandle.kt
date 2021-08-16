package com.skillw.pouvoir.api.handle

import com.skillw.pouvoir.api.annotation.PManager
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.util.ClassUtils
import java.lang.reflect.Field
import java.lang.reflect.Modifier

object PManagerHandle {
    fun isPManagerField(field: Field): Boolean {
        return field.isAnnotationPresent(PManager::class.java)
    }

    fun getImpl(field: Field): String? {
        if (!isPManagerField(field)) {
            return null
        }
        return field.getAnnotation(PManager::class.java).impl
    }

    fun getParams(field: Field, obj: Any?): Array<Any>? {
        if (!isPManagerField(field)) {
            return null
        }
        val clazz = field.declaringClass
        val list = field.getAnnotation(PManager::class.java).params
        var fail = false
        val params = Array<Any>(list.size) {
            val key = list[it]
            if (key == "this") obj!!
            try {
                clazz.getDeclaredField(key).get(obj)
            } catch (e: Exception) {
                fail = true
                e.printStackTrace()
            }
        }
        return if (fail) null else params
    }

    fun getPManagers(subPouvoir: SubPouvoir): Set<Manager> {
        val pManagers = HashSet<Manager>()
        for (field in subPouvoir.javaClass.fields) {
            field.isAccessible = true
            if (isPManagerField(field) && field.get(subPouvoir) != null)
                pManagers.add(field.get(subPouvoir) as Manager)
            field.isAccessible = false
        }
        return pManagers
    }

    fun voluation(obj: Any?, field: Field) {
        val clazz = field.type
        field.isAccessible = true
        val aImplClassName = getImpl(field)
        val implClassName =
            if (aImplClassName == "undefined")
                "${obj?.javaClass?.`package`?.name}.manager.${clazz.simpleName}Impl"
            else aImplClassName ?: return
        val implClass: Class<*> = try {
            Class.forName(implClassName)
        } catch (e: Exception) {
            if (!Modifier.isAbstract(clazz.modifiers)) clazz else null
        } ?: return
        val params = getParams(field, obj) ?: return
        val paramsTypes = Array<Class<*>>(params.size) {
            params[it].javaClass
        }
        val impl =
            if (ClassUtils.isObj(implClass)) implClass.getDeclaredField("INSTANCE").get(null)
            else if (paramsTypes.isNotEmpty()) implClass.getConstructor(*paramsTypes).newInstance(*params)
            else implClass.getConstructor().newInstance()
        field.set(obj, impl)
    }
}