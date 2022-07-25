package com.skillw.pouvoir.api

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.annotation.ScriptTopLevel
import org.bukkit.entity.LivingEntity

object PouvoirAPI {
    @ScriptTopLevel
    @JvmStatic
    fun String.analysis(): String {
        return Pouvoir.inlineFunctionManager.analysis(this)
    }

    @ScriptTopLevel
    @JvmStatic
    fun String.placeholder(entity: LivingEntity): String {
        return Pouvoir.pouPlaceHolderAPI.replace(entity, this)
    }
}