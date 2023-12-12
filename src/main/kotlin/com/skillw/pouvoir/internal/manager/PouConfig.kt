package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.ConfigManager
import com.skillw.pouvoir.api.plugin.map.DataMap
import com.skillw.pouvoir.util.toMap
import org.spigotmc.AsyncCatcher
import taboolib.common.platform.Platform
import taboolib.common.platform.function.getDataFolder
import taboolib.module.metrics.Metrics
import taboolib.module.metrics.charts.AdvancedPie
import taboolib.module.metrics.charts.MultiLineChart
import taboolib.module.metrics.charts.SingleLineChart
import taboolib.platform.util.bukkitPlugin
import java.io.File

object PouConfig : ConfigManager(Pouvoir) {
    override val priority = 0

    val numberFormat: String
        get() {
            return this["config"].getString("options.number-format")!!
        }
    val databaseConfig: DataMap
        get() = DataMap().also { it.putAll(this["config"].getConfigurationSection("database")!!.toMap()) }

    override fun onActive() {
        AsyncCatcher.enabled = false
    }

    var debugMode = false

    val debug: Boolean
        get() = debugMode || this["config"].getBoolean("options.debug")

    val strAppendSeparator: String
        get() = this["config"].getString("options.operation.string-append-separator") ?: ", "


    override fun onLoad() {
        AsyncCatcher.enabled = false
        createIfNotExists(
            "dispatchers", "custom-trigger.yml"
        )
        createIfNotExists(
            "handlers", "simple-handler.yml"
        )
        createIfNotExists(
            "scripts", "example.js", "groovy.groovy", "test.asahi",
            "core/asahi.js",
            "core/basic.js",
            "core/util/ray_trace.js",
            "core/util/color.js",
            "core/util/container.js",
            "core/util/message.js",
            "core/util/number.js",
            "core/util/placeholder.js",
            "core/util/player.js",
            "conditions/level.js",
            "conditions/permission.js",
            "conditions/altitude.js",
            "conditions/food.js",
            "conditions/water.js",
            "conditions/health.js",
            "conditions/world.js",
            "conditions/fire.js",
            "conditions/weather.js",
            "conditions/ground.js",
            "conditions/player.js",
        )
        if (!File(getDataFolder(),"core").exists())
            create(
                "scripts",
                "core/asahi.js",
                "core/basic.js",
                "core/util/ray_trace.js",
                "core/util/color.js",
                "core/util/container.js",
                "core/util/message.js",
                "core/util/number.js",
                "core/util/placeholder.js",
                "core/util/player.js"
            )
    }

    override fun onEnable() {
        metrics()
    }

    private fun metrics() {
        Metrics(14180, bukkitPlugin.description.version, Platform.BUKKIT).run {
            addCustomChart(SingleLineChart("script_listeners") {
                Pouvoir.listenerManager.size
            })
            addCustomChart(MultiLineChart("scripts") {
                val map = HashMap<String, Int>()
                Pouvoir.scriptEngineManager.keys.forEach {
                    map[it] = Pouvoir.scriptManager.filterValues { script -> script.pouEngine.key == it }.size
                }
                map
            })
            addCustomChart(AdvancedPie("scripts_using") {
                val map = HashMap<String, Int>()
                Pouvoir.scriptEngineManager.keys.forEach {
                    map[it] = Pouvoir.scriptManager.filterValues { script -> script.pouEngine.key == it }.size
                }
                map
            })
        }
    }

    override fun subReload() {
        completeYaml("config.yml")
    }

    @JvmStatic
    fun debug(debug: () -> Unit) {
        if (PouConfig.debug) {
            debug.invoke()
        }
    }
}