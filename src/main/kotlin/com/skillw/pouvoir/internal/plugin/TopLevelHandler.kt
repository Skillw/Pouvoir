package com.skillw.pouvoir.internal.plugin

import com.skillw.pouvoir.api.annotation.ScriptTopLevel
import com.skillw.pouvoir.api.plugin.handler.ClassHandler
import com.skillw.pouvoir.api.script.ScriptTool
import com.skillw.pouvoir.internal.manager.ScriptEngineManagerImpl.globalVariables
import com.skillw.pouvoir.util.ClassUtils.static
import org.bukkit.plugin.Plugin
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.library.reflex.ReflexClass
import java.util.function.Function

@RuntimeDependencies(RuntimeDependency("!org.jetbrains.kotlin:kotlin-reflect:1.6.10"))
object TopLevelHandler : ClassHandler(1) {
    private fun String.ifEmpty(key: String) = ifEmpty { key }

    override fun inject(clazz: Class<*>, plugin: Plugin) {
        val structure = ReflexClass.of(clazz).structure
        if (structure.isAnnotationPresent(ScriptTopLevel::class.java)) {
            val key = structure.getAnnotation(ScriptTopLevel::class.java)?.property<String>("key") ?: ""
            globalVariables[key.ifEmpty(clazz.simpleName)] = clazz.static()
        }
        structure.fields
            .filter { it.isStatic && it.isAnnotationPresent(ScriptTopLevel::class.java) }
            .forEach {
                val key = structure.getAnnotation(ScriptTopLevel::class.java)?.property<String>("key") ?: ""
                globalVariables[key.ifEmpty(it.name)] = it.get(null) ?: return@forEach
            }
        structure.methods
            .filter { it.isStatic && it.isAnnotationPresent(ScriptTopLevel::class.java) }
            .forEach { method ->
                val key = structure.getAnnotation(ScriptTopLevel::class.java)?.property<String>("key") ?: ""
                globalVariables[key.ifEmpty(method.name)] = Function { it: Any? ->
                    val args = ScriptTool.arrayOf(it)
                    return@Function method.invokeStatic(*args)
                }
            }
    }
}
