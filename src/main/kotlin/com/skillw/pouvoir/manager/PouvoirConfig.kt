package com.skillw.pouvoir.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.plugin
import com.skillw.pouvoir.api.manager.sub.ConfigManager
import com.skillw.pouvoir.util.ClassUtils
import com.skillw.pouvoir.util.MessageUtils.wrong
import taboolib.module.lang.sendLang
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object PouvoirConfig : ConfigManager(Pouvoir) {
    override val priority = 0
    val staticClasses = ConcurrentHashMap<String, Any>()
    override val isCheckVersion = this["config"].getBoolean("options.check-version")

    fun reloadStaticClasses() {
        staticClasses.clear()
        val script = this["script"]
        val staticSection = script.getConfigurationSection("static-classes")
        for (key in staticSection.getKeys(false)) {
            var path = staticSection[key].toString()
            val isObj = path.endsWith(";object")
            path = path.replace(";object", "")
            var staticClass = ClassUtils.staticClass(path)
            if (staticClass == null) {
                Pouvoir.console.sendLang("static-class-not-found")
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

    fun createIfNotExists(name: String, vararg fileNames: String) {
        val dir = File(plugin.dataFolder.path + "/$name")
        if (!dir.exists()) {
            dir.mkdir()
            for (fileName in fileNames) {
                plugin.saveResource("$name/$fileName", true)
            }
        }
    }

    override fun init() {
        createIfNotExists("effects", "example.yml")
        createIfNotExists("scripts", "example.js", "fun.js")
        reloadStaticClasses()
    }

    override fun reload() {
        reloadStaticClasses()
    }
}