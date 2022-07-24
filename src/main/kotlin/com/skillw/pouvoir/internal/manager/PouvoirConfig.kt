package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.ConfigManager
import com.skillw.pouvoir.util.ClassUtils
import com.skillw.pouvoir.util.MessageUtils.warning
import org.spigotmc.AsyncCatcher
import taboolib.common.platform.Platform
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import taboolib.module.metrics.Metrics
import taboolib.module.metrics.charts.SingleLineChart
import taboolib.platform.BukkitPlugin
import java.util.concurrent.ConcurrentHashMap

object PouvoirConfig : ConfigManager(Pouvoir) {
    override val priority = 0


    private val staticClasses = ConcurrentHashMap<String, Any>()

    val numberFormat: String
        get() {
            return this["config"].getString("options.number-format")!!
        }
    val scale: Int
        get() = this["config"].getInt("options.big-decimal-scale")

    private fun reloadStaticClasses() {
        val globalVars = Pouvoir.scriptEngineManager.globalVariables
        staticClasses.forEach {
            globalVars.remove(it.key)
        }
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
                    warning("The $path is not a object!")
                    continue
                }
            }
            staticClasses[key] = staticClass
        }
        globalVars.putAll(staticClasses)
    }

    val debug: Boolean
        get() = this["config"].getBoolean("options.debug")


    override fun onLoad() {
        AsyncCatcher.enabled = false
        createIfNotExists("scripts", "example.js", "groovy.groovy")
        reloadStaticClasses()
    }

    override fun onEnable() {
        metrics()
    }

    private fun metrics() {
        val metrics = Metrics(14180, BukkitPlugin.getInstance().description.version, Platform.BUKKIT).run {
            addCustomChart(SingleLineChart("scripts") {
                Pouvoir.scriptManager.size
            })
        }
    }

    override fun subReload() {
        reloadStaticClasses()
    }
}