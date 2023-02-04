package com.skillw.pouvoir.internal.core.plugin

import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.annotation.PouManager
import com.skillw.pouvoir.util.instance
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
        val subPouvoir = clazz.instance as? SubPouvoir? ?: return null
        for (field in fields.filter { field -> isPouManagerField(field) }) {
            field.isAccessible = true
            val mainPackage = subPouvoir.javaClass.`package`?.name
            val managerName = field.type.simpleName

            val pouManagerClass = field.type
            val pManager = field.getAnnotation(PouManager::class.java)
            val implClass: Class<*> =
                if (!Modifier.isAbstract(pouManagerClass.modifiers)) pouManagerClass
                else kotlin.runCatching { Class.forName(pManager.path.ifEmpty { "$mainPackage.internal.manager.${managerName}Impl" }) }
                    .getOrNull() ?: continue
            val pouManager = implClass.instance
            if (pouManager == null) {
                warning("Can't find the PouManager ImplClass ${implClass.name} !")
                continue
            }
            field.set(subPouvoir, pouManager)
        }
        return subPouvoir
    }

}