package com.skillw.pouvoir.api

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.annotation.ScriptTopLevel
import com.skillw.pouvoir.api.function.context.IContext
import com.skillw.pouvoir.internal.function.TextHandler
import com.skillw.pouvoir.internal.function.context.SimpleContext
import com.skillw.pouvoir.internal.manager.PouFunctionManagerImpl
import org.bukkit.entity.LivingEntity

object PouvoirAPI {
    @ScriptTopLevel
    @JvmStatic
    fun String.analysis(): String {
        return TextHandler.analysis(this)
    }

    @JvmStatic
    fun String.analysis(context: IContext = SimpleContext()): String {
        return TextHandler.analysis(this, context)
    }

    @ScriptTopLevel
    @JvmStatic
    fun String.parse(): String {
        return PouFunctionManagerImpl.parse(this, context = SimpleContext())?.toString() ?: this
    }


    @JvmStatic
    fun String.parse(
        namespaces: Array<String> = arrayOf("common"),
        context: IContext = SimpleContext(),
    ): String {
        return Pouvoir.pouFunctionManager.parse(this, namespaces = namespaces, context = context)?.toString() ?: this
    }

    @ScriptTopLevel
    @JvmStatic
    fun String.placeholder(entity: LivingEntity): String {
        return Pouvoir.pouPlaceHolderAPI.replace(entity, this)
    }

    @JvmStatic
    fun String.placeholder(entity: LivingEntity, analysis: Boolean = true): String {
        return Pouvoir.pouPlaceHolderAPI.replace(entity, this, analysis)
    }
}