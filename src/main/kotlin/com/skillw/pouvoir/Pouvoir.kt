package com.skillw.pouvoir


import com.skillw.pouvoir.api.annotation.PouManager
import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.api.manager.sub.ContainerManager
import com.skillw.pouvoir.api.manager.sub.ListenerManager
import com.skillw.pouvoir.api.manager.sub.PouPlaceHolderAPI
import com.skillw.pouvoir.api.manager.sub.PouPlaceHolderManager
import com.skillw.pouvoir.api.manager.sub.function.PouActionManager
import com.skillw.pouvoir.api.manager.sub.function.PouFunctionManager
import com.skillw.pouvoir.api.manager.sub.message.MessagerBuilderManager
import com.skillw.pouvoir.api.manager.sub.message.PersonalManager
import com.skillw.pouvoir.api.manager.sub.script.*
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.thread.BasicThreadFactory
import com.skillw.pouvoir.internal.manager.PouConfig
import com.skillw.pouvoir.internal.manager.PouConfig.threadPoolSize
import org.bukkit.plugin.java.JavaPlugin
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.module.nms.MinecraftVersion
import taboolib.platform.BukkitPlugin
import java.util.concurrent.ScheduledThreadPoolExecutor


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
    val sync by lazy {
        MinecraftVersion.majorLegacy >= 11900
    }


    /** Config */
    @Config(migrate = true, autoReload = true)
    lateinit var config: ConfigFile

    @Config("script.yml", true, autoReload = true)
    lateinit var script: ConfigFile

    /** Managers */
    override lateinit var managerData: ManagerData

    @JvmStatic
    @PouManager
    lateinit var configManager: PouConfig

    @JvmStatic
    @PouManager
    lateinit var compileManager: CompileManager

    @JvmStatic
    @PouManager
    lateinit var pouFunctionManager: PouFunctionManager

    @JvmStatic
    @PouManager
    lateinit var pouActionManager: PouActionManager

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


    @JvmStatic
    @PouManager
    lateinit var messagerBuilderManager: MessagerBuilderManager

    @JvmStatic
    @PouManager
    lateinit var personalManager: PersonalManager

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
}