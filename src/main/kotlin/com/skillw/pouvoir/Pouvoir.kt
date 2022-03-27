package com.skillw.pouvoir

import com.skillw.pouvoir.api.annotation.PManager
import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.api.manager.sub.*
import com.skillw.pouvoir.api.manager.sub.script.CompileManager
import com.skillw.pouvoir.api.manager.sub.script.ScriptAnnotationManager
import com.skillw.pouvoir.api.manager.sub.script.ScriptEngineManager
import com.skillw.pouvoir.api.manager.sub.script.ScriptManager
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.thread.BasicThreadFactory
import com.skillw.pouvoir.internal.manager.PouvoirConfig
import com.skillw.pouvoir.util.FileUtils
import com.skillw.pouvoir.util.MessageUtils
import com.skillw.pouvoir.util.MessageUtils.info
import com.skillw.pouvoir.util.Pair
import org.bukkit.configuration.file.YamlConfiguration
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitPlugin
import java.io.File
import java.util.concurrent.ScheduledThreadPoolExecutor


object Pouvoir : Plugin(), SubPouvoir {

    override val key = "Pouvoir"
    override val plugin by lazy {
        BukkitPlugin.getInstance()
    }
    override val poolExecutor by lazy {
        ScheduledThreadPoolExecutor(
            20,
            BasicThreadFactory.Builder().daemon(true).namingPattern("pouvoir-schedule-pool-%d").build()
        )
    }

    override fun getConfigs(): MutableMap<String, Pair<File, YamlConfiguration>> {
        val map = HashMap<String, Pair<File, YamlConfiguration>>()
        for (field in this::class.java.fields) {
            if (!field.isAnnotationPresent(Config::class.java)) continue
            map[field.name] =
                Pair(
                    (field.get(this) as Configuration).file!!,
                    FileUtils.loadConfigFile((field.get(this) as Configuration).file)!!
                )
        }
        return map
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
    @PManager
    lateinit var configManager: PouvoirConfig

    @JvmStatic
    @PManager
    lateinit var functionManager: FunctionManager

    @JvmStatic
    @PManager
    lateinit var pouPlaceholderManager: PouPlaceHolderManager

    @JvmStatic
    @PManager
    lateinit var pouPlaceHolderAPI: PouPlaceHolderAPI

    @JvmStatic
    @PManager
    lateinit var listenerManager: ListenerManager

    @JvmStatic
    @PManager
    lateinit var scriptEngineManager: ScriptEngineManager

    @JvmStatic
    @PManager
    lateinit var compileManager: CompileManager

    @JvmStatic
    @PManager
    lateinit var scriptAnnotationManager: ScriptAnnotationManager

    @JvmStatic
    @PManager
    lateinit var scriptManager: ScriptManager


    @JvmStatic
    @PManager
    lateinit var playerDataManager: PlayerDataManager

    fun isDepend(plugin: org.bukkit.plugin.Plugin) =
        plugin.description.depend.contains("Pouvoir") || plugin.description.softDepend.contains("Pouvoir")

    override fun onLoad() {
        load()
        info("&d[&9Pouvoir&d] &aPouvoir is loaded...")
    }

    override fun onEnable() {
        enable()
        info("&d[&9Pouvoir&d] &aPouvoir is enabled...")
    }

    override fun onActive() {
        active()
    }

    override fun onDisable() {
        disable()
        info("&d[&9Pouvoir&d] &aPouvoir is disabled...")
    }

    @JvmStatic
    fun debug(string: String) {
        if (configManager.debug) {
            MessageUtils.debug(string)
        }
    }
}