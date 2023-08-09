package com.skillw.pouvoir.api.plugin

import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.plugin.handler.ClassHandler
import com.skillw.pouvoir.api.plugin.map.KeyMap
import com.skillw.pouvoir.api.plugin.map.component.Registrable
import com.skillw.pouvoir.internal.core.plugin.SubPouvoirHandler
import com.skillw.pouvoir.util.existClass
import com.skillw.pouvoir.util.instance
import com.skillw.pouvoir.util.plugin.PluginUtils
import com.skillw.pouvoir.util.safe
import com.skillw.pouvoir.util.static
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.library.reflex.ClassStructure
import taboolib.library.reflex.ReflexClass
import taboolib.platform.util.bukkitPlugin
import java.util.concurrent.ConcurrentHashMap

object TotalManager : KeyMap<SubPouvoir, ManagerData>() {
    internal val pluginData = ConcurrentHashMap<Plugin, SubPouvoir>()
    val allStaticClasses = ConcurrentHashMap<String, Any>()
    private val allClasses = HashSet<ClassStructure>()

    @Awake(LifeCycle.LOAD)
    fun load() {
        Bukkit.getPluginManager().plugins
            .filter { isDependPouvoir(it) }
            .sortedWith { p1, p2 ->
                if (p1.isDepend(p2)) 1 else -1
            }
            .forEach {
                safe { loadSubPou(it) }
            }
        allClasses.forEach { clazz ->
            handlers.forEach {
                it.inject(clazz)
            }
        }
    }

    private val handlers = ArrayList<ClassHandler>()

    private fun loadSubPou(plugin: Plugin) {
        if (!isDependPouvoir(plugin)) return

        val classes = PluginUtils.getClasses(plugin::class.java).map { ReflexClass.of(it).structure }

        classes.forEach {
            kotlin.runCatching { allStaticClasses[it.simpleName.toString()] = it.owner.static() }
        }
        allClasses.addAll(classes)

        handlers.addAll(classes
            .filter { ClassHandler::class.java.isAssignableFrom(it.owner) && it.simpleName != "ClassHandler" }
            .mapNotNull {
                it.owner.instance as? ClassHandler?
            })

        classes.forEach classFor@{ clazz ->
            //优先加载Managers
            safe { SubPouvoirHandler.inject(clazz, plugin) }
        }
        pluginData[plugin]?.let {
            ManagerData(it).register()
        }

        classes.filter { clazz ->
            clazz.isAnnotationPresent(AutoRegister::class.java)
        }.forEach { clazz ->
            kotlin.runCatching {
                val auto = clazz.getAnnotation(AutoRegister::class.java)
                val test = auto.property<String>("test") ?: ""
                if ((test.isEmpty() || test.existClass()))
                    (clazz.owner.instance as? Registrable<*>?)?.register()
            }.exceptionOrNull()?.printStackTrace()
        }
    }

    private fun Plugin.isDepend(other: Plugin) =
        description.depend.contains(other.name) || description.softDepend.contains(other.name)

    private fun isDependPouvoir(plugin: Plugin): Boolean {
        return plugin.isDepend(bukkitPlugin) || plugin.name == "Pouvoir"
    }
}