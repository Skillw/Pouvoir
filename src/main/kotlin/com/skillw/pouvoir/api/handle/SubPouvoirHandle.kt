package com.skillw.pouvoir.api.handle

import com.skillw.pouvoir.api.manager.TotalManager
import com.skillw.pouvoir.api.plugin.SubPouvoir
import org.bukkit.plugin.Plugin
import java.lang.reflect.Field

object SubPouvoirHandle {
    fun handle(clazz: Class<*>, plugin: Plugin) {
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
                PManagerHandle.voluation(obj, field)
            }
            TotalManager.pluginData[plugin] = (obj ?: plugin) as SubPouvoir
        }
    }
}