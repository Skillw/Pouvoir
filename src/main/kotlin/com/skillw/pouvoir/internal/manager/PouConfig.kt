package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.ConfigManager
import org.spigotmc.AsyncCatcher
import taboolib.common.platform.Platform
import taboolib.module.metrics.Metrics
import taboolib.module.metrics.charts.AdvancedPie
import taboolib.module.metrics.charts.MultiLineChart
import taboolib.module.metrics.charts.SingleLineChart
import taboolib.platform.BukkitPlugin

object PouConfig : ConfigManager(Pouvoir) {
    override val priority = 0


    val numberFormat: String
        get() {
            return this["config"].getString("options.number-format")!!
        }
    val defaultMessage: String
        get() = this["config"].getString("options.message.default", "chat")!!
    val lockedDefaultMessage: Boolean
        get() = this["config"].getBoolean("options.message.locked-to-default")

    val scale: Int
        get() = this["config"].getInt("options.big-decimal-scale")

    override fun onActive() {
        AsyncCatcher.enabled = false
    }


    val threadPoolSize by lazy {
        this["config"].getInt("options.thread-pool-size", 4)
    }
    val debug: Boolean
        get() = this["config"].getBoolean("options.debug")

    val debugFunc: Boolean
        get() = this["config"].getBoolean("options.debug-function")

    override fun onLoad() {
        AsyncCatcher.enabled = false
//        runCatching {
//            File(getDataFolder(), "scripts/core").delete()
//        }
        createIfNotExists(
            "scripts", "example.js", "groovy.groovy",
            "core/inline_func.js",
            "core/basic.js",
            "core/tool.js",
            "core/util/ray_trace.js",
            "core/util/color.js",
            "core/util/container.js",
            "core/util/menu.js",
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
        Metrics(14180, BukkitPlugin.getInstance().description.version, Platform.BUKKIT).run {
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

    @JvmStatic
    fun debug(debug: () -> Unit) {
        if (this.debug) {
            debug.invoke()
        }
    }
}