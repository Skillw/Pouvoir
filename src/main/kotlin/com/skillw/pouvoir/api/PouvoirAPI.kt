package com.skillw.pouvoir.api

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.annotation.ScriptTopLevel
import com.skillw.pouvoir.internal.function.TextHandler
import org.bukkit.entity.LivingEntity

object PouvoirAPI {
    @ScriptTopLevel
    @JvmStatic
    fun String.analysis(): String {
        return TextHandler.analysis(Pouvoir.inlineFunctionManager.legacyAnalysis(this))
    }

    @ScriptTopLevel
    @JvmStatic
    fun String.parse(): String {
        return Pouvoir.pouFunctionManager.parse(this)
    }

    @ScriptTopLevel
    @JvmStatic
    fun String.placeholder(entity: LivingEntity): String {
        return Pouvoir.pouPlaceHolderAPI.replace(entity, this)
    }
}