package com.skillw.pouvoir.internal.plugin

import com.skillw.pouvoir.api.annotation.PouManager
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.util.ClassUtils.findClass
import taboolib.common.platform.function.warning
import java.lang.reflect.Field
import java.lang.reflect.Modifier

object PouManagerUtils {
    private fun isPouManagerField(field: Field) = field.isAnnotationPresent(PouManager::class.java)

    fun SubPouvoir.getPouManagers(): Set<Manager> = this.javaClass.fields
        .filter { field -> isPouManagerField(field) && field.get(this) != null }
        .map { field -> field.get(this) as Manager }
        .toSet()

    internal fun initPouManagers(clazz: Class<*>): SubPouvoir? {
        val fields = clazz.fields
        val subPouvoir = clazz.getField("INSTANCE").get(null) as? SubPouvoir? ?: return null
        for (field in fields.filter { field -> isPouManagerField(field) }) {
            field.isAccessible = true
            val mainPackage = subPouvoir.javaClass.`package`?.name
            val managerName = field.type.simpleName

            val pouManagerClass = field.type
            val implClass: Class<*> =
                if (!Modifier.isAbstract(pouManagerClass.modifiers)) pouManagerClass
                else "$mainPackage.internal.manager.${managerName}Impl".findClass()
                    ?: continue
            val pouManager = implClass.getField("INSTANCE").get(null)
            if (pouManager == null) {
                warning("Can't find the PouManager ImplClass ${implClass.name} !")
                continue
            }
            field.set(subPouvoir, pouManager)
        }
        return subPouvoir
    }

}