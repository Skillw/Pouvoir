package com.skillw.pouvoir

import com.skillw.pouvoir.api.annotation.PouManager
import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.api.manager.sub.*
import com.skillw.pouvoir.api.manager.sub.script.*
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.thread.BasicThreadFactory
import com.skillw.pouvoir.internal.manager.PouConfig
import com.skillw.pouvoir.internal.manager.PouConfig.debugPrefix
import com.skillw.pouvoir.internal.manager.PouConfig.threadPoolSize
import com.skillw.pouvoir.util.MessageUtils
import org.bukkit.plugin.java.JavaPlugin
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.Plugin
import taboolib.module.chat.colored
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.platform.BukkitPlugin
import java.util.concurrent.ScheduledThreadPoolExecutor

@RuntimeDependencies(RuntimeDependency("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2"))
object Pouvoir : Plugin(), SubPouvoir {

    override val key = "Pouvoir"

    override val plugin: JavaPlugin by lazy {
        BukkitPlugin.getInstance()
    }

    val poolExecutor by lazy {
        ScheduledThreadPoolExecutor(
            threadPoolSize,
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
    lateinit var configManager: PouConfig

    @JvmStatic
    @PouManager
    lateinit var compileManager: CompileManager

    @JvmStatic
    @PouManager
    lateinit var inlineFunctionManager: InlineFunctionManager

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

    @JvmStatic
    @PouManager
    lateinit var scriptTaskManager: ScriptTaskManager


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
    fun debug(debug: () -> String) {
        if (configManager.debug) {
            MessageUtils.info((debugPrefix + debug.invoke()).colored())
        }
    }
}