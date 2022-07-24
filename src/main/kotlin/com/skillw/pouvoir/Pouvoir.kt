package com.skillw.pouvoir

import com.skillw.pouvoir.api.annotation.PouManager
import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.api.manager.sub.*
import com.skillw.pouvoir.api.manager.sub.script.ScriptAnnotationManager
import com.skillw.pouvoir.api.manager.sub.script.ScriptEngineManager
import com.skillw.pouvoir.api.manager.sub.script.ScriptManager
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.thread.BasicThreadFactory
import com.skillw.pouvoir.internal.manager.PouvoirConfig
import org.bukkit.plugin.java.JavaPlugin
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.module.chat.colored
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.module.lang.asLangText
import taboolib.platform.BukkitPlugin
import java.util.concurrent.ScheduledThreadPoolExecutor

object Pouvoir : Plugin(), SubPouvoir {

    override val key = "Pouvoir"

    override val plugin: JavaPlugin by lazy(LazyThreadSafetyMode.NONE) {
        BukkitPlugin.getInstance()
    }

    val poolExecutor by lazy(LazyThreadSafetyMode.NONE) {
        ScheduledThreadPoolExecutor(
            20,
            BasicThreadFactory.Builder().daemon(true).namingPattern("pouvoir-schedule-pool-%d").build()
        )
    }


    /**
     * Config
     */
    @Config(migrate = true, autoReload = true)
    lateinit var config: ConfigFile

    @Config("script.yml", true, autoReload = true)
    lateinit var script: ConfigFile

    /**
     * Managers
     */
    override lateinit var managerData: ManagerData

    @JvmStatic
    @PouManager
    lateinit var configManager: PouvoirConfig

    @JvmStatic
    @PouManager
    lateinit var functionManager: FunctionManager

    @JvmStatic
    @PouManager
    lateinit var pouPlaceholderManager: PouPlaceHolderManager

    @JvmStatic
    @PouManager
    lateinit var pouPlaceHolderAPI: PouPlaceHolderAPI

    @JvmStatic
    @PouManager
    lateinit var listenerManager: ListenerManager

    @JvmStatic
    @PouManager
    lateinit var scriptEngineManager: ScriptEngineManager

    @JvmStatic
    @PouManager
    lateinit var scriptAnnotationManager: ScriptAnnotationManager

    @JvmStatic
    @PouManager
    lateinit var scriptManager: ScriptManager


    @JvmStatic
    @PouManager
    lateinit var containerManager: ContainerManager

    fun isDepend(plugin: org.bukkit.plugin.Plugin) =
        plugin.description.depend.contains("Pouvoir") || plugin.description.softDepend.contains("Pouvoir")

    override fun onLoad() {
        load()
    }

    override fun onEnable() {
        enable()
    }

    override fun onActive() {
        active()
    }

    override fun onDisable() {
        disable()
    }

    @JvmStatic
    fun debug(string: String) {
        if (configManager.debug) {
            console().sendMessage(console().asLangText("plugin-debug") + string.colored())
        }
    }
}