package com.skillw.pouvoir.api.handle

import com.skillw.pouvoir.api.plugin.SubPouvoir
import taboolib.module.configuration.Config
import taboolib.module.configuration.SecuredFile

object ConfigHandle {
    fun getConfigs(subPouvoir: SubPouvoir): MutableMap<String, SecuredFile> {
        val map = HashMap<String, SecuredFile>()
        for (field in subPouvoir.javaClass.fields) {
            if (!field.isAnnotationPresent(Config::class.java)) continue
            map[field.name] = field.get(subPouvoir) as SecuredFile
        }
        return map
    }
}