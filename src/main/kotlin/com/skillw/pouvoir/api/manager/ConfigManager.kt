package com.skillw.pouvoir.api.manager

import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.internal.handle.DefaultableHandle
import com.skillw.pouvoir.util.MessageUtils.wrong
import org.bukkit.configuration.file.YamlConfiguration
import taboolib.common.platform.function.getDataFolder
import taboolib.common5.FileWatcher
import taboolib.module.lang.Language
import taboolib.module.lang.LanguageFile
import taboolib.module.lang.Type
import java.io.File

abstract class ConfigManager(override val subPouvoir: SubPouvoir) : Manager,
    BaseMap<String, YamlConfiguration>() {
    val fileMap = BaseMap<File, YamlConfiguration>()
    val defaults = HashSet<Class<*>>()
    override operator fun get(key: String): YamlConfiguration {
        val result = super.get(key)
        if (result == null) {
            wrong("The config $key dose not exist in the SubPouvoir ${subPouvoir.key}!")
            return YamlConfiguration.loadConfiguration(getDataFolder())
        }
        return result
    }

    private val watcher: FileWatcher = FileWatcher.INSTANCE

    abstract fun defaultOptions(): Map<String, Map<String, Any>>

    init {
        subPouvoir.getConfigs().forEach {
            val key = it.key
            val value = it.value
            fileMap.register(value.first, value.second)
            this.register(key, value.second)
        }
        defaults.forEach { DefaultableHandle.inject(it, subPouvoir.plugin) }
        for (it in fileMap.keys) {
            if (watcher.hasListener(it)) {
                watcher.removeListener(it)
            }
            watcher.addSimpleListener(it) {
                val yaml = fileMap[it]!!
                yaml.load(it)
                this[it.nameWithoutExtension] = yaml
            }
        }
    }

    private fun default(configKey: String, config: YamlConfiguration) {
        val map = defaultOptions()
        if (!map.containsKey(configKey)) {
            return
        }
        val defaultOptions = map[configKey]!!
        for ((key, value) in defaultOptions) {
            if (config.contains(key)) continue
            config[key] = value
        }
    }

    final override fun onReload() {
        fileMap.forEach {
            val file = it.key
            val config = it.value
            config.load(file)
            val key = it.key.nameWithoutExtension
            default(key, config)
            config.save(file)
            this[key] = config
        }
        subReload()
    }

    protected open fun subReload() {}

    val serverFile: File by lazy {
        File(subPouvoir.plugin.dataFolder.parentFile.absolutePath.toString().replace("\\plugins", ""))
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

    open val isCheckVersion: Boolean = false

    fun getLang(path: String): Type? {
        return getLocal().nodes[path]
    }

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
        val dir = File(subPouvoir.plugin.dataFolder.path + "/$name")
        if (!dir.exists()) {
            dir.mkdir()
            for (fileName in fileNames) {
                subPouvoir.plugin.saveResource("$name/$fileName", true)
            }
        }
    }
}