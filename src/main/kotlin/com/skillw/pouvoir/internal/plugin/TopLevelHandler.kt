package com.skillw.pouvoir.internal.plugin

import com.skillw.pouvoir.api.annotation.ScriptTopLevel
import com.skillw.pouvoir.api.plugin.handler.ClassHandler
import com.skillw.pouvoir.api.script.ScriptTool
import com.skillw.pouvoir.api.script.ScriptTool.toObject
import com.skillw.pouvoir.internal.script.common.top.TopLevel
import com.skillw.pouvoir.internal.script.common.top.TopLevelData
import com.skillw.pouvoir.util.ClassUtils.static
import com.skillw.pouvoir.util.MessageUtils.information
import org.bukkit.plugin.Plugin
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.library.reflex.ReflexClass
import java.util.function.Function

@RuntimeDependencies(RuntimeDependency("!org.jetbrains.kotlin:kotlin-reflect:1.6.10"))
object TopLevelHandler : ClassHandler(1) {
    private fun String.ifEmpty(key: String) = ifEmpty { key }

    override fun inject(clazz: Class<*>, plugin: Plugin) {
        val source = plugin.name
        val structure = ReflexClass.of(clazz).structure
        if (structure.isAnnotationPresent(ScriptTopLevel::class.java)) {
            val annotation = structure.getAnnotation(ScriptTopLevel::class.java) ?: return
            val key = (annotation.property<String>("key") ?: "").ifEmpty(clazz.simpleName)
            val description = annotation.property<String>("description")
            val member = clazz.static()
            val clazzStr = "&f$key &7= &6${clazz.name}"
            val info = "$clazzStr &8$description"
            TopLevelData(key, source, TopLevel.Type.CLASS, member, info).register()
        }
        structure.fields
            .filter { it.isStatic && it.isAnnotationPresent(ScriptTopLevel::class.java) }
            .forEach {
                val annotation = it.getAnnotation(ScriptTopLevel::class.java) ?: return
                val key = (annotation.property<String>("key") ?: "").ifEmpty(it.name)
                val description = annotation.property<String>("description")
                val member = it.get(null) ?: return@forEach
                val info = "${member.information(key)} &8$description"
                TopLevelData(key, source, TopLevel.Type.FIELD, member, info).register()
            }
        structure.methods
            .filter { it.isStatic && it.isAnnotationPresent(ScriptTopLevel::class.java) }
            .forEach { method ->
                val annotation = method.getAnnotation(ScriptTopLevel::class.java) ?: return
                val key = (annotation.property<String>("key") ?: "").ifEmpty(method.name)
                val description = annotation.property<String>("description")
                val paramTypes = method.parameterTypes
                val paramStr = StringBuilder()
                paramTypes.forEachIndexed { index, clazz ->
                    paramStr.append("&6" + clazz.simpleName)
                    if (index != paramTypes.lastIndex) paramStr.append("&f, ")
                }
                val returnStr = "&6" + method.returnType.simpleName.run { if (this == "void") "Unit" else this }
                val info = "&f$key &7: &f($paramStr&f) &f-> $returnStr &8$description"
                TopLevelData(key, source, TopLevel.Type.FUNCTION, Function { it: Any? ->
                    val single = method.parameterTypes.size == 1
                    return@Function if (single) {
                        if (it?.javaClass?.simpleName == "ScriptObjectMirror")
                            method.invokeStatic(it.toObject())
                        else
                            method.invokeStatic(it)
                    } else {
                        method.invokeStatic(*ScriptTool.arrayOf(it))
                    }
                }, info).register()
            }
    }
}
