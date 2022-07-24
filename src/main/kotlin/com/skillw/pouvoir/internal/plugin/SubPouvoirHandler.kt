package com.skillw.pouvoir.internal.plugin

import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.TotalManager
import com.skillw.pouvoir.api.plugin.handler.ClassHandler
import org.bukkit.plugin.Plugin

object SubPouvoirHandler : ClassHandler(0) {
    override fun inject(clazz: Class<*>, plugin: Plugin) {
        if (SubPouvoir::class.java.isAssignableFrom(clazz) && clazz.simpleName != "SubPouvoir")
            TotalManager.pluginData[plugin] = PouManagerUtils.initPouManagers(clazz) ?: return
    }
}
