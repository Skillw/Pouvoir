package com.skillw.pouvoir.api.plugin

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.able.Registrable
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.annotation.ScriptTopLevel
import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.api.plugin.handler.ClassHandler
import com.skillw.pouvoir.util.ClassUtils.existClass
import com.skillw.pouvoir.util.ClassUtils.findClass
import com.skillw.pouvoir.util.ClassUtils.static
import com.skillw.pouvoir.util.PluginUtils
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.library.reflex.ReflexClass
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object TotalManager : KeyMap<SubPouvoir, ManagerData>() {
    internal val pluginData = ConcurrentHashMap<Plugin, SubPouvoir>()
    val allStaticClasses = ConcurrentHashMap<String, Any>()

    @ScriptTopLevel
    @JvmStatic
    fun static(name: String): Any? {
        return if (!name.contains("."))
            allStaticClasses[name]
        else
            name.findClass()?.static()
    }

    @Awake(LifeCycle.LOAD)
    fun load() {
        Bukkit.getPluginManager().plugins.filter { dependPouvoir(it) }.forEach {
            try {
                loadSubPou(it)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val handlers = LinkedList<ClassHandler>()

    private fun loadSubPou(plugin: Plugin) {
        if (!dependPouvoir(plugin)) return

        val classes = PluginUtils.getClasses(plugin).map { ReflexClass.of(it).structure }

        classes.forEach {
            kotlin.runCatching { allStaticClasses[it.simpleName.toString()] = it.owner.static() }
        }

        handlers.addAll(classes
            .filter { ClassHandler::class.java.isAssignableFrom(it.owner) && it.simpleName != "ClassHandler" }
            .mapNotNull {
                it.getField("INSTANCE").get(null) as? ClassHandler?
            })

        classes.forEach classFor@{ clazz ->
            handlers
                .forEach { handler ->
                    try {
                        handler.inject(clazz.owner, plugin)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
        }
        pluginData[plugin]?.let {
            ManagerData(it).register()
        }

        classes.filter { clazz ->
            clazz.isAnnotationPresent(AutoRegister::class.java)
        }.forEach { clazz ->
            kotlin.runCatching {
                val auto = clazz.getAnnotation(AutoRegister::class.java)
                val test = auto.property("test", "")
                if (test.isNotEmpty()) {
                    kotlin.runCatching { Class.forName(test) }
                }
                (clazz.getField("INSTANCE").get(null) as? Registrable<*>?)?.register()
            }
        }
        classes
            .forEach { clazz ->
                clazz.fields.forEach { field ->
                    if (field.isAnnotationPresent(AutoRegister::class.java)) {
                        kotlin.runCatching {
                            val autoRegister = field.getAnnotation(AutoRegister::class.java)
                            val test = autoRegister.property<String>("test") ?: ""
                            val obj = field.get()
                            if (obj is Registrable<*> && (test.isEmpty() || test.existClass())) obj.register()
                        }
                    }
                }
            }
    }

    fun dependPouvoir(plugin: Plugin): Boolean {
        return Pouvoir.isDepend(plugin) || plugin.name == "Pouvoir"
    }
}