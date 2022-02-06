package com.skillw.pouvoir.api.plugin

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.internal.handle.DefaultableHandle
import com.skillw.pouvoir.internal.handle.SubPouvoirHandle
import com.skillw.pouvoir.util.PluginUtils
import org.bukkit.Bukkit
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.plugin.Plugin
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer

object TotalManager : KeyMap<SubPouvoir, ManagerData>() {
    internal val pluginData = ConcurrentHashMap<Plugin, SubPouvoir>()


    fun remove(plugin: Plugin) {
        val subPouvoir = pluginData[plugin] ?: return
        this.remove(subPouvoir)
        pluginData.remove(plugin)
    }

    private fun load(plugin: Plugin) {
        remove(plugin)
        init(plugin)
        register(plugin)
    }


    fun isSubPouvoir(plugin: Plugin): Boolean {
        return Pouvoir.isDepend(plugin) || plugin.name == "Pouvoir"
    }

    private val allClasses = HashSet<Class<*>>()

    fun forEachClass(consumer: Consumer<Class<*>>) {
        allClasses.forEach {
            consumer.accept(it)
        }
    }

    fun init(plugin: Plugin) {
        if (!isSubPouvoir(plugin)) {
            return
        }
        val classes: List<Class<*>> = PluginUtils.getClasses(plugin)
        for (clazz in classes) {
            SubPouvoirHandle.inject(clazz, plugin)
            DefaultableHandle.inject(clazz, plugin)
            if (ConfigurationSerializable::class.java.isAssignableFrom(clazz)) {
                ConfigurationSerialization.registerClass(clazz.asSubclass(ConfigurationSerializable::class.java))
            }
        }
        allClasses.addAll(classes)
    }

    @Awake(LifeCycle.LOAD)
    fun onLoading() {
        Bukkit.getPluginManager().plugins.forEach { load(it) }
        map.values.forEach {
            it.load()
        }
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