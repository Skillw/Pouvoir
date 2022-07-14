package com.skillw.pouvoir.util

import com.skillw.pouvoir.Pouvoir
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.configuration.serialization.ConfigurationSerializable
import taboolib.common.platform.function.warning
import java.io.File
import java.util.*

object FileUtils {

    @JvmStatic
    fun listFiles(file: File): List<File> {
        val files: MutableList<File> = ArrayList()
        if (file.isDirectory) {
            file.listFiles()?.forEach { files.addAll(listFiles(it)) }
        } else {
            files.add(file)
        }
        return files
    }

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <T : ConfigurationSerializable> loadMultiply(mainFile: File?, clazz: Class<T>): List<Pair<T, File>> {
        return mainFile?.run {
            val list = LinkedList<Pair<T, File>>()
            for (file in getYamlsFromFile(this)!!) {
                val config = loadConfigFile(file) ?: continue
                for (key in config.getKeys(false)) {
                    list.add(
                        Pair(
                            (clazz.getMethod(
                                "deserialize",
                                org.bukkit.configuration.ConfigurationSection::class.java
                            ).invoke(null, config[key]!!) as? T? ?: continue), file
                        )
                    )
                }
            }
            list
        } ?: emptyList()
    }

    @JvmStatic
    fun ConfigurationSection.toMap(): Map<String, Any> {
        val newMap = HashMap<String, Any>()
        for (it in this.getKeys(false)) {
            val value = this[it]
            if (value is ConfigurationSection) {
                newMap[it] = value.toMap()
                continue
            }
            newMap[it] = value!!
        }
        return newMap
    }

    @JvmStatic
    fun loadConfigFile(file: File?): YamlConfiguration? {
        return file?.run {
            val config = YamlConfiguration()
            try {
                config.load(this)
            } catch (e: Exception) {
                warning("Wrong config! in $name")
                warning("Cause: " + ColorUtils.unColor(e.cause!!.message.toString()))
                return null
            }
            return config
        }

    }

    @JvmStatic
    fun getYamlsFromFile(file: File?): List<File>? {
        return file?.run {
            val files: MutableList<File> = ArrayList()
            val allFiles = listFiles() ?: return files
            for (subFile in allFiles) {
                if (subFile.isFile && subFile.name.endsWith(".yml")) {
                    files.add(subFile)
                    continue
                }
                files.addAll(getYamlsFromFile(subFile)!!)
            }
            return files
        }

    }

    @JvmStatic
    fun pathNormalize(file: File): String {
        return file.absolutePath.replace(Pouvoir.configManager.serverFile.absolutePath, "").replace("\\", "/")
    }
}