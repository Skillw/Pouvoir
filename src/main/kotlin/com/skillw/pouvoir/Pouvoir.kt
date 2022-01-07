package com.skillw.pouvoir

import com.skillw.pouvoir.api.annotation.PManager
import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.api.manager.sub.*
import com.skillw.pouvoir.api.manager.sub.script.CompileManager
import com.skillw.pouvoir.api.manager.sub.script.ScriptManager
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.manager.PouvoirConfig
import com.skillw.pouvoir.util.MessageUtils.info
import jdk.nashorn.api.scripting.NashornScriptEngineFactory
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.concurrent.BasicThreadFactory
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitAdapter
import taboolib.platform.BukkitPlugin
import java.util.concurrent.ScheduledThreadPoolExecutor
import javax.script.ScriptEngine

object Pouvoir : Plugin(), SubPouvoir {

    override val key = "Pouvoir"
    override val plugin by lazy {
        BukkitPlugin.getInstance()
    }
    override val poolExecutor: ScheduledThreadPoolExecutor by lazy {
        ScheduledThreadPoolExecutor(
            20,
            BasicThreadFactory.Builder().namingPattern("$key-schedule-pool-%d").daemon(true).build()
        )
    }

    val version by lazy {
        Bukkit.getServer().javaClass.getPackage().name.replace(".", ",").split(",").toTypedArray()[3]
    }

    val console by lazy {
        BukkitAdapter().console()
    }

    val isLegacy by lazy {
        version.split("_").toTypedArray()[1].toInt() < 13
    }

    @JvmStatic
    val scriptEngine: ScriptEngine by lazy {
        try {
            if (Class.forName("jdk.nashorn.api.scripting.NashornScriptEngineFactory") == null) {
                org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory().scriptEngine
            } else {
                NashornScriptEngineFactory().scriptEngine
            }
        } catch (e: Exception) {
            org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory().scriptEngine
        }
    }

    /**
     * Config
     */
    @Config(migrate = true)
    lateinit var config: Configuration

    @Config("script.yml", true)
    lateinit var script: Configuration

    /**
     * Managers
     */
    override lateinit var managerData: ManagerData

    @JvmStatic
    @PManager
    lateinit var configManager: PouvoirConfig

    @JvmStatic
    @PManager
    lateinit var compileManager: CompileManager

    @JvmStatic
    @PManager
    lateinit var scriptManager: ScriptManager

    @JvmStatic
    @PManager
    lateinit var functionManager: FunctionManager

    @JvmStatic
    val placeholderDataManager = PlaceHolderDataManager

    @JvmStatic
    @PManager
    lateinit var rpgPlaceHolderAPI: RPGPlaceHolderAPI

    @JvmStatic
    @PManager
    lateinit var listenerManager: ListenerManager

    @JvmStatic
    @PManager
    lateinit var baseAttributeManager: BaseAttributeManager

    override fun onLoad() {
        info("&d[&9Pouvoir&d] &aPouvoir is loading...")
    }

    override fun onEnable() {
        info("&d[&9Pouvoir&d] &aPouvoir is enabling...")
    }

    override fun onDisable() {
        info("&d[&9Pouvoir&d] &aPouvoir is disabling...")
    }

    fun isDepend(plugin: org.bukkit.plugin.Plugin) =
        plugin.description.depend.contains("Pouvoir") || plugin.description.softDepend.contains("Pouvoir")
}