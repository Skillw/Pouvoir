package com.skillw.pouvoir.api.plugin

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.able.Registrable
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.api.plugin.handler.ClassHandler
import com.skillw.pouvoir.util.PluginUtils
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object TotalManager : KeyMap<SubPouvoir, ManagerData>() {
    internal val pluginData = ConcurrentHashMap<Plugin, SubPouvoir>()

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

        val classes = PluginUtils.getClasses(plugin)
        handlers.addAll(classes
            .filter { ClassHandler::class.java.isAssignableFrom(it) && it.simpleName != "ClassHandler" }
            .mapNotNull {
                it.getField("INSTANCE").get(null) as? ClassHandler?
            })

        classes.forEach classFor@{ clazz ->
            handlers
                .forEach { handler ->
                    try {
                        handler.inject(clazz, plugin)
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
            try {
                (clazz.getField("INSTANCE").get(null) as? Registrable<*>?)?.register()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun dependPouvoir(plugin: Plugin): Boolean {
        return Pouvoir.isDepend(plugin) || plugin.name == "Pouvoir"
    }
}