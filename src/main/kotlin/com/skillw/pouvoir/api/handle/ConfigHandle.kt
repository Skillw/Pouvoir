package com.skillw.pouvoir.api.handle

import com.skillw.pouvoir.api.plugin.SubPouvoir
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration

object ConfigHandle {
    fun getConfigs(subPouvoir: SubPouvoir): MutableMap<String, Configuration> {
        val map = HashMap<String, Configuration>()
        for (field in subPouvoir.javaClass.fields) {
            if (!field.isAnnotationPresent(Config::class.java)) continue
            map[field.name] = field.get(subPouvoir) as Configuration
        }
        return map
    }
}