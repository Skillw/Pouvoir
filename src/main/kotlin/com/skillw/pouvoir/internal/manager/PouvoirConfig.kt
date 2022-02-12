package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.ConfigManager
import com.skillw.pouvoir.util.ClassUtils
import com.skillw.pouvoir.util.MessageUtils.wrong
import org.spigotmc.AsyncCatcher
import taboolib.common.io.newFile
import taboolib.common.platform.Platform
import taboolib.common.platform.function.console
import taboolib.common.platform.function.getDataFolder
import taboolib.expansion.setupPlayerDatabase
import taboolib.module.lang.sendLang
import taboolib.module.metrics.Metrics
import taboolib.module.metrics.charts.SingleLineChart
import taboolib.platform.BukkitPlugin
import java.util.concurrent.ConcurrentHashMap

object PouvoirConfig : ConfigManager(Pouvoir) {
    override val priority = 0


    val staticClasses = ConcurrentHashMap<String, Any>()
    override var isCheckVersion: Boolean
        get() = this["config"].getBoolean("options.check-version")
        set(value) {}

    val numberFormat: String
        get() {
            return this["config"].getString("options.number-format")!!
        }
    val scale: Int
        get() = this["config"].getInt("options.big-decimal-scale")

    fun reloadStaticClasses() {
        staticClasses.clear()
        val script = this["script"]
        val staticSection = script.getConfigurationSection("static-classes")!!
        for (key in staticSection.getKeys(false)) {
            var path = staticSection[key].toString()
            val isObj = path.endsWith(";object")
            path = path.replace(";object", "")
            var staticClass = ClassUtils.staticClass(path)
            if (staticClass == null) {
                console().sendLang("static-class-not-found", path)
                continue
            }
            if (isObj) {
                val clazz = staticClass.javaClass.getMethod("getRepresentedClass").invoke(staticClass) as Class<*>
                val field = clazz.getField("INSTANCE")
                staticClass = field.get(null)
                if (staticClass == null) {
                    wrong("The $path is not a object!")
                    continue
                }
            }
            staticClasses[key] = staticClass
        }
    }


    override fun onInit() {
        AsyncCatcher.enabled = false
        createIfNotExists("effects", "example.yml")
        createIfNotExists("scripts", "example.js", "groovy.groovy")
        reloadStaticClasses()
    }

    override fun onEnable() {
        if (this["config"].getBoolean("database.enable")) {
            setupPlayerDatabase(Pouvoir.config.getConfigurationSection("database")!!)
        } else {
            setupPlayerDatabase(newFile(getDataFolder(), "data.db"))
        }
        metrics()
    }

    private fun metrics() {
        val metrics = Metrics(14180, BukkitPlugin.getInstance().description.version, Platform.BUKKIT).run {
            addCustomChart(SingleLineChart("scripts") {
                Pouvoir.scriptManager.size
            })
        }
    }

    override fun defaultOptions(): Map<String, Map<String, Any>> = mapOf(
        "config" to mapOf(
            "options" to mapOf(
                "number-format" to ".##",
                "options.big-decimal-scale" to 4
            ),
            "database" to mapOf(
                "enable" to false,
                "host" to "localhost",
                "port" to 3306,
                "user" to "root",
                "password" to "root",
                "database" to "root",
                "table" to "my_database"
            )
        ),
        "script" to mapOf(
            "static-classes" to mapOf(
                "Bukkit" to "org.bukkit.Bukkit",
                "Arrays" to "java.util.Arrays",
                "Tool" to "com.skillw.pouvoir.api.script.ScriptTool",
                "Data" to "com.skillw.pouvoir.api.script.ScriptTool;object",
                "Pouvoir" to "com.skillw.pouvoir.Pouvoir",
                "CalculationUtils" to "com.skillw.pouvoir.util.CalculationUtils",
                "MapUtils" to "com.skillw.pouvoir.util.MapUtils",
                "ColorUtils" to "com.skillw.pouvoir.util.ColorUtils",
                "EDCodeUtils" to "com.skillw.pouvoir.util.EDCodeUtils",
                "EntityUtils" to "com.skillw.pouvoir.util.EntityUtils",
                "FileUtils" to "com.skillw.pouvoir.util.FileUtils",
                "ItemUtils" to "com.skillw.pouvoir.util.ItemUtils",
                "GsonUtils" to "com.skillw.pouvoir.util.GsonUtils",
                "MessageUtils" to "com.skillw.pouvoir.util.MessageUtils",
                "NumberUtils" to "com.skillw.pouvoir.util.NumberUtils",
                "PlayerUtils" to "com.skillw.pouvoir.util.PlayerUtils",
                "ClassUtils" to "com.skillw.pouvoir.util.ClassUtils",
                "StringUtils" to "com.skillw.pouvoir.util.StringUtils"
            )
        )
    )

    override fun subReload() {
        reloadStaticClasses()
    }
}