package com.skillw.pouvoir.api.manager

import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.map.BaseMap
import com.skillw.pouvoir.util.completeYaml
import com.skillw.pouvoir.util.listSubFiles
import com.skillw.pouvoir.util.loadYaml
import com.skillw.pouvoir.util.plugin.Pair
import com.skillw.pouvoir.util.safe
import org.bukkit.configuration.file.YamlConfiguration
import taboolib.common.platform.function.getDataFolder
import taboolib.common5.FileWatcher
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.module.lang.Language
import java.io.File

/**
 * Config manager
 *
 * @constructor Create empty Config manager
 * @property subPouvoir
 */
abstract class ConfigManager(final override val subPouvoir: SubPouvoir) : Manager,
    BaseMap<String, YamlConfiguration>() {
    override val key = "ConfigManager"
    private val fileMap = BaseMap<File, YamlConfiguration>()
    private val watcher = FileWatcher()

    /** Server file */
    val serverDirectory: File by lazy {
        File(
            getDataFolder().parentFile.absolutePath.toString().replace("\\plugins", "")
        )
    }

    init {
        val map = HashMap<String, Pair<File, YamlConfiguration>>()
        //Init Map
        for (field in subPouvoir::class.java.fields) {
            if (field.annotations.all { it.annotationClass.simpleName != "Config" }) continue
            val file = field.get(subPouvoir).getProperty<File>("file") ?: continue
            map[field.name] = Pair(file, file.loadYaml()!!)
        }
        //Register Config
        map.forEach { (key, pair) ->
            val (file, yaml) = pair
            fileMap.register(file, yaml)
            this.register(key, yaml)
        }
        for (file in fileMap.keys) {
            if (watcher.hasListener(file)) {
                watcher.removeListener(file)
            }
            watcher.addSimpleListener(file) {
                val yaml = fileMap[file]!!
                safe { yaml.load(file) }
                this[file.nameWithoutExtension] = yaml
            }
        }
    }

    override operator fun get(key: String): YamlConfiguration =
        super.get(key) ?: error("The config $key dose not exist in the SubPouvoir ${subPouvoir.key}!")

    /** Sub reload */
    protected open fun subReload() {}

    final override fun onReload() {
        val dir = File(Language.path)
        dir.listSubFiles().filter { it.extension == "yml" }.forEach(this::completeYaml)
        Language.reload()
        subReload()
    }


    /**
     * Create if not exists
     *
     * @param name
     * @param fileNames
     */
    fun create(name: String, vararg fileNames: String) {
        val path = subPouvoir.plugin.dataFolder.path
        val dir = File("$path/$name")
        dir.mkdir()
        for (fileName in fileNames) {
            safe { subPouvoir.plugin.saveResource("$name/$fileName", true) }
        }
    }

    /**
     * Create if not exists
     *
     * @param name
     * @param fileNames
     */
    fun createIfNotExists(name: String, vararg fileNames: String) {
        val path = subPouvoir.plugin.dataFolder.path
        val dir = File("$path/$name")
        if (!dir.exists()) {
            dir.mkdir()
            for (fileName in fileNames) {
                safe { subPouvoir.plugin.saveResource("$name/$fileName", true) }
            }
        }
    }

    @Deprecated("Deprecated", ReplaceWith(""))
    fun completeYaml(file: File, ignores: Set<String> = emptySet()): Map<String, Any?> = emptyMap()
//        subPouvoir.plugin.completeYaml(file, ignores)
    @Deprecated("Deprecated", ReplaceWith(""))
    fun completeYaml(filePath: String, ignores: Set<String> = emptySet()): Map<String, Any?> =emptyMap()
//        subPouvoir.plugin.completeYaml(filePath, ignores)
}
