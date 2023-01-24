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
import net.minecraft.server.v1_12_R1.NBTCompressedStreamTools
import org.bukkit.plugin.java.JavaPlugin
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.getDataFolder
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.module.nms.MinecraftVersion
import taboolib.platform.util.bukkitPlugin
import java.io.File
import java.io.FileInputStream


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

    @JvmStatic
    @PouManager
    lateinit var configManager: PouConfig

    @JvmStatic
    @PouManager
    lateinit var compileManager: CompileManager

    @JvmStatic
    @PouManager
    lateinit var placeholderManager: PouPlaceholderManager

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
    lateinit var databaseManager: DatabaseManager

    @JvmStatic
    @PouManager
    lateinit var triggerManager: TriggerManager

    @JvmStatic
    @PouManager
    lateinit var triggerHandlerManager: TriggerHandlerManager

    @JvmStatic
    @PouManager
    lateinit var selectorManager: SelectorManager

    @JvmStatic
    @PouManager
    lateinit var hologramManager: HologramManager

    fun printNBT() {
        val playerDataFolder = File(getDataFolder().getParentFile().getParentFile(), "world\\playerdata\\")

        val uuid: String = "0cb207d2-402b-3a85-889b-baa507e308dd"

        val playerDat = File(playerDataFolder, "$uuid.dat")

        val inputStream = FileInputStream(playerDat)

        val nbt = NBTCompressedStreamTools.a(inputStream)

        File(getDataFolder(), "playerData.yml").apply {
            createNewFile()
            writeText(nbt.toString())
        }
    }

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