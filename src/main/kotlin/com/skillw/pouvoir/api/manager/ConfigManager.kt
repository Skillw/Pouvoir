package com.skillw.pouvoir.api.manager

import com.skillw.pouvoir.Pouvoir.plugin
import com.skillw.pouvoir.api.handle.ConfigHandle
import com.skillw.pouvoir.api.handle.DefaultableHandle
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.util.MessageUtils.wrong
import taboolib.module.configuration.Configuration
import taboolib.module.lang.Language
import taboolib.module.lang.LanguageFile
import java.io.File

abstract class ConfigManager(private val subPouvoir: SubPouvoir) : Manager,
    BaseMap<String, Configuration>() {
    val defaults = HashSet<Class<*>>()
    override operator fun get(key: String): Configuration {
        val result = super.get(key)
        if (result == null) {
            wrong("The config $key dose not exist in the SubPouvoir ${subPouvoir.key}!")
            return Configuration.empty()
        }
        return result
    }

    init {
        defaults.forEach { DefaultableHandle.handle(it, subPouvoir.plugin) }
    }

    init {
        ConfigHandle.getConfigs(subPouvoir).forEach { register(it.key, it.value) }
    }

    val serverFile: File by lazy {
        File(plugin.dataFolder.parentFile.absolutePath.toString().replace("\\plugins", ""))
    }

    override val key = "ConfigManager"
    val version by lazy {
        var version = subPouvoir.plugin.description.version.replace(".", "")
        if (version.length == 3) {
            version += "0"
        }
        version.toInt()
    }
    val language: String
        get() {
            val lang: String = Language.getLocale()
            return "languages/$lang/"
        }

    abstract val isCheckVersion: Boolean

    companion object {
        @JvmStatic
        fun getLocal(): LanguageFile {
            return Language.languageFile.entries.firstOrNull { it.key.equals(Language.getLocale(), true) }?.value
                ?: Language.languageFile.values.firstOrNull()!!
        }

        @JvmStatic
        fun getPluginPrefix(): String {
            val local = getLocal()
            return local.nodes["plugin-prefix"].toString()
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
}