package com.skillw.pouvoir


import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.api.manager.sub.*
import com.skillw.pouvoir.api.manager.sub.script.CompileManager
import com.skillw.pouvoir.api.manager.sub.script.ScriptAnnotationManager
import com.skillw.pouvoir.api.manager.sub.script.ScriptEngineManager
import com.skillw.pouvoir.api.manager.sub.script.ScriptManager
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.annotation.PouManager
import com.skillw.pouvoir.internal.manager.PouConfig
import org.bukkit.plugin.java.JavaPlugin
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.module.nms.MinecraftVersion
import taboolib.platform.util.bukkitPlugin


/**
 * Pouvoir 的主类 提供API
 *
 * @constructor Create empty Pouvoir
 */
object Pouvoir : SubPouvoir, Plugin() {
    override val key = "Pouvoir"

    override val plugin: JavaPlugin by lazy {
        bukkitPlugin
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

    
    @PouManager
    lateinit var configManager: PouConfig

    
    @PouManager
    lateinit var compileManager: CompileManager

    
    @PouManager
    lateinit var placeholderManager: PouPlaceholderManager

    
    @PouManager
    lateinit var listenerManager: ListenerManager

    
    @PouManager
    lateinit var scriptEngineManager: ScriptEngineManager

    
    @PouManager
    lateinit var scriptAnnotationManager: ScriptAnnotationManager

    
    @PouManager
    lateinit var scriptManager: ScriptManager

    
    @PouManager
    lateinit var databaseManager: DatabaseManager

    
    @PouManager
    lateinit var triggerManager: TriggerManager

    
    @PouManager
    lateinit var triggerHandlerManager: TriggerHandlerManager

    
    @PouManager
    lateinit var selectorManager: SelectorManager

    
    @PouManager
    lateinit var hologramManager: HologramManager


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