package com.skillw.pouvoir.internal.handle

import com.skillw.pouvoir.api.annotation.PManager
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.TotalManager
import com.skillw.pouvoir.util.ClassUtils.isObj
import org.bukkit.plugin.Plugin
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

    fun violation(obj: Any?, field: Field) {
        val clazz = field.type
        field.isAccessible = true
        val aImplClassName = getImpl(field)
        var implClassName =
            if (aImplClassName == "undefined")
                "${obj?.javaClass?.`package`?.name}.manager.${clazz.simpleName}Impl"
            else aImplClassName ?: return
        val implClass: Class<*> = try {
            Class.forName(implClassName)
        } catch (e: Exception) {
            try {
                implClassName = "${obj?.javaClass?.`package`?.name}.internal.manager.${clazz.simpleName}Impl"
                Class.forName(implClassName)
            } catch (e: Exception) {
                if (!Modifier.isAbstract(clazz.modifiers)) clazz else null
            } ?: return
        } ?: return
        val params = getParams(field, obj) ?: return
        val paramsTypes = Array<Class<*>>(params.size) {
            params[it].javaClass
        }
        val impl =
            if (implClass.isObj()) implClass.getDeclaredField("INSTANCE").get(null)
            else if (paramsTypes.isNotEmpty()) implClass.getConstructor(*paramsTypes).newInstance(*params)
            else implClass.getConstructor().newInstance()
        field.set(obj, impl)
    }
}

object SubPouvoirHandle {

    fun inject(clazz: Class<*>, plugin: Plugin) {
        if (SubPouvoir::class.java.isAssignableFrom(clazz) && clazz.simpleName != "SubPouvoir") {
            val fields = clazz.fields
            val fieldsBeSet = HashSet<Field>()
            var obj: Any? = null
            field@ for (field in fields) {
                if (field.name == "INSTANCE") {
                    obj = field.get(null)
                    continue@field
                }
                if (!PManagerHandle.isPManagerField(field)) {
                    continue@field
                }

                fieldsBeSet.add(field)
            }
            for (field in fieldsBeSet) {
                field.isAccessible = true
                PManagerHandle.violation(obj, field)
            }
            val subPouvoir = (obj ?: plugin) as SubPouvoir
            TotalManager.pluginData[plugin] = subPouvoir
        }
    }
}