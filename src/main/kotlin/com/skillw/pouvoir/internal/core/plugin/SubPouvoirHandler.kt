package com.skillw.pouvoir.internal.core.plugin

import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.TotalManager
import org.bukkit.plugin.Plugin
import taboolib.library.reflex.ClassStructure

object SubPouvoirHandler {
    fun inject(clazz: ClassStructure, plugin: Plugin) {
        val owner = clazz.owner
        if (SubPouvoir::class.java.isAssignableFrom(owner.instance) && clazz.simpleName != "SubPouvoir")
            TotalManager.pluginData[plugin] = PouManagerUtils.initPouManagers(owner.instance!!) ?: return
    }
}
