package com.skillw.pouvoir.internal.core.plugin

import com.skillw.pouvoir.api.annotation.ScriptTopLevel
import com.skillw.pouvoir.api.plugin.handler.ClassHandler
import com.skillw.pouvoir.api.script.ScriptTool
import com.skillw.pouvoir.api.script.ScriptTool.toObject
import com.skillw.pouvoir.api.script.top.TopAPI
import com.skillw.pouvoir.util.ClassUtils.static
import com.skillw.pouvoir.util.MessageUtils.information
import org.bukkit.plugin.Plugin
import taboolib.library.reflex.ReflexClass
import java.util.function.Function

object TopLevelHandler : ClassHandler(1) {
    private fun String.ifEmpty(key: String) = ifEmpty { key }

    override fun inject(clazz: Class<*>, plugin: Plugin) {
        val source = plugin.name
        val structure = ReflexClass.of(clazz).structure
        if (clazz.isAnnotationPresent(ScriptTopLevel::class.java)) {
            val annotation = clazz.getAnnotation(ScriptTopLevel::class.java) ?: return
            val key = (annotation.key).ifEmpty(clazz.simpleName)
            val description = annotation.description
            val member = clazz.static()
            val clazzStr = "&f$key &7= &6${clazz.name}"
            TopAPI.addClass(key, member, source, clazzStr, "&8$description")
        }
        structure.fields
            .filter { it.isStatic && it.isAnnotationPresent(ScriptTopLevel::class.java) }
            .forEach {
                val annotation = it.getAnnotation(ScriptTopLevel::class.java)
                val key = (annotation.property<String>("key"))?.ifEmpty(it.name).toString()
                val description = annotation.property<String>("description").toString()
                val member = it.get(null) ?: return@forEach
                val info = member.information(key)
                TopAPI.addObject(key, member, source, info, "&8$description")
            }
        structure.methods
            .filter { it.isStatic && it.isAnnotationPresent(ScriptTopLevel::class.java) }
            .forEach { method ->
                val annotation = method.getAnnotation(ScriptTopLevel::class.java)
                val key = (annotation.property<String>("key"))?.ifEmpty(method.name).toString()
                val description = annotation.property<String>("description").toString()
                val paramTypes = method.parameterTypes
                val paramStr = StringBuilder()
                paramTypes.forEachIndexed { index, clazz ->
                    paramStr.append("&6" + clazz.simpleName)
                    if (index != paramTypes.lastIndex) paramStr.append("&f, ")
                }
                val returnStr = "&6" + method.returnType.simpleName.run { if (this == "void") "Unit" else this }
                val info = "&f$key &7: &f($paramStr&f) &f-> $returnStr"
                TopAPI.addFunction(key, Function { it: Any? ->
                    val single = method.parameterTypes.size == 1
                    return@Function if (single) {
                        if (it?.javaClass?.simpleName == "ScriptObjectMirror")
                            method.invokeStatic(it.toObject())
                        else
                            method.invokeStatic(it)
                    } else {
                        method.invokeStatic(*ScriptTool.arrayOf(it))
                    }
                }, source, info, "&8$description")
            }
    }
}
