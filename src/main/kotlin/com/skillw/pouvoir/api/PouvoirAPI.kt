package com.skillw.pouvoir.api

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.annotation.ScriptTopLevel
import com.skillw.pouvoir.api.function.context.IContext
import com.skillw.pouvoir.internal.core.function.TextHandler
import com.skillw.pouvoir.internal.core.function.context.SimpleContext
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
    fun String.eval(): Any {
        return PouFunctionManagerImpl.eval(this, context = SimpleContext()) ?: this
    }

    @JvmStatic
    fun String.eval(
        namespaces: Array<String> = arrayOf("common"),
        context: IContext = SimpleContext(),
    ): Any {
        return Pouvoir.pouFunctionManager.eval(this, namespaces = namespaces, context = context) ?: this
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