package com.skillw.pouvoir.api.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.handle.DefaultableHandle
import com.skillw.pouvoir.api.handle.SubPouvoirHandle
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.util.JarUtils
import com.skillw.pouvoir.util.PluginUtils
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import java.util.concurrent.ConcurrentHashMap

object TotalManager : KeyMap<SubPouvoir, ManagerData>() {
    internal val pluginData = ConcurrentHashMap<Plugin, SubPouvoir>()

    private fun load(plugin: Plugin) {
        remove(plugin)
        init(plugin)
        register(plugin)
    }

    fun remove(plugin: Plugin) {
        val subPouvoir = pluginData[plugin] ?: return
        removeByKey(subPouvoir)
        pluginData.remove(plugin)
    }


    @Awake(LifeCycle.LOAD)
    fun onLoading() {
        JarUtils.NashornLib()
        Bukkit.getPluginManager().plugins.forEach { load(it) }

        map.values.forEach { it.load() }
    }

    @Awake(LifeCycle.ENABLE)
    fun onEnable() {
        map.values.forEach {
            it.enable()
        }
    }

    @Awake(LifeCycle.ACTIVE)
    fun onActive() {
        map.values.forEach {
            it.active()
        }
    }

    private fun init(plugin: Plugin) {
        if (Pouvoir.isDepend(plugin) || plugin.name == "Pouvoir") {
            val classes: List<Class<*>> = PluginUtils.getClasses(plugin)
            for (clazz in classes) {
                SubPouvoirHandle.handle(clazz, plugin)
                DefaultableHandle.handle(clazz, plugin)
            }
        }
    }

    fun register(plugin: Plugin) {
        val subPouvoir = pluginData[plugin] ?: return
        ManagerData(subPouvoir).register()
    }

    fun reload(subPouvoir: SubPouvoir) {
        subPouvoir.managerData.reload()
    }

    fun disable(subPouvoir: SubPouvoir) {
        subPouvoir.managerData.disable()
    }

    @Awake(LifeCycle.DISABLE)
    fun disableAll() {
        map.values.forEach { it.disable() }
    }
}