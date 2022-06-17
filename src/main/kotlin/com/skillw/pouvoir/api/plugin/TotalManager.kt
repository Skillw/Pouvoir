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
    private val allClasses = HashSet<Class<*>>()


    private fun load(plugin: Plugin) {
        remove(plugin)
        init(plugin)
        register(plugin)
    }

    fun remove(plugin: Plugin) {
        val subPouvoir = pluginData[plugin] ?: return
        this.remove(subPouvoir)
        pluginData.remove(plugin)
    }

    @Awake(LifeCycle.LOAD)
    fun onLoading() {
        Bukkit.getPluginManager().plugins.forEach { load(it) }
    }

    fun forEachClass(consumer: Consumer<Class<*>>) {
        allClasses.forEach {
            consumer.accept(it)
        }
    }


    private fun init(plugin: Plugin) {
        try {
            if (!isSubPouvoir(plugin)) return
            val classes = PluginUtils.getClasses(plugin)
            for (clazz in classes) {
                SubPouvoirHandle.inject(clazz, plugin)
                DefaultableHandle.inject(clazz, plugin)
                if (ConfigurationSerializable::class.java.isAssignableFrom(clazz)) {
                    ConfigurationSerialization.registerClass(clazz.asSubclass(ConfigurationSerializable::class.java))
                }
            }
            allClasses.addAll(classes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun register(plugin: Plugin) {
        pluginData[plugin]?.apply {
            ManagerData(this).register()
        } ?: return

    }

    fun isSubPouvoir(plugin: Plugin): Boolean {
        return Pouvoir.isDepend(plugin) || plugin.name == "Pouvoir"
    }
}