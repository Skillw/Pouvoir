package com.skillw.pouvoir.util

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.util.MessageUtils.wrong
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.configuration.serialization.ConfigurationSerializable
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
        if (mainFile == null) {
            return emptyList()
        }
        val list = LinkedList<Pair<T, File>>()
        for (file in getYamlsFromFile(mainFile)!!) {
            val config = loadConfigFile(file) ?: continue
            for (key in config.getKeys(false)) {
                list.add(
                    (clazz.getMethod(
                        "deserialize",
                        org.bukkit.configuration.ConfigurationSection::class.java
                    ).invoke(null, config[key]!!) as? T? ?: continue) to file
                )
            }
        }
        return list
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
        if (file == null) {
            return null
        }
        val config = YamlConfiguration()
        try {
            config.load(file)
        } catch (e: Exception) {
            wrong("Wrong config! in ${file.name}")
            wrong("Cause: " + ColorUtils.unColor(e.cause!!.message.toString()))
            return null
        }
        return if (config.getKeys(false).isEmpty()) {
            null
        } else config
    }

    @JvmStatic
    fun getYamlsFromFile(file: File?): List<File>? {
        if (file == null) {
            return null
        }
        val files: MutableList<File> = ArrayList()
        val allFiles = file.listFiles() ?: return files
        for (subFile in allFiles) {
            if (subFile.isFile && subFile.name.endsWith(".yml")) {
                files.add(subFile)
                continue
            }
            files.addAll(getYamlsFromFile(subFile)!!)
        }
        return files
    }

    @JvmStatic
    fun pathNormalize(file: File): String {
        return file.absolutePath.replace(Pouvoir.configManager.serverFile.absolutePath, "").replace("\\", "/")
    }
}