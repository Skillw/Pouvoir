package com.skillw.pouvoir.util

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.able.Keyable
import com.skillw.pouvoir.util.MessageUtils.wrong
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.*
import java.lang.reflect.InvocationTargetException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.logging.Level

object FileUtils {
    @JvmStatic
    fun listFiles(file: File): List<File> {
        val files: MutableList<File> = ArrayList()
        if (file.isDirectory) {
            for (listFile in file.listFiles()) {
                files.addAll(listFiles(listFile))
            }
        } else {
            files.add(file)
        }
        return files
    }

    @JvmOverloads
    @JvmStatic
    fun readFromFile(file: File, size: Int = 1024, encode: Charset = StandardCharsets.UTF_8): String? {
        return try {
            val fileInputStream = FileInputStream(file)
            val content: String?
            try {
                val bin = BufferedInputStream(fileInputStream)
                content = try {
                    readFromStream(fileInputStream, size, encode)
                } catch (throwable: Throwable) {
                    try {
                        bin.close()
                    } catch (var8: Throwable) {
                        throwable.addSuppressed(var8)
                    }
                    throw throwable
                }
                bin.close()
            } catch (throwable: Throwable) {
                try {
                    fileInputStream.close()
                } catch (var7: Throwable) {
                    throwable.addSuppressed(var7)
                }
                throw throwable
            }
            fileInputStream.close()
            content
        } catch (exception: IOException) {
            exception.printStackTrace()
            null
        }
    }

    @JvmStatic
    fun readFromStream(`in`: InputStream, size: Int, encode: Charset): String? {
        return try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            val content: String
            try {
                val b = ByteArray(size)
                while (true) {
                    var i: Int
                    if (`in`.read(b).also { i = it } <= 0) {
                        content = byteArrayOutputStream.toString(encode.name())
                        break
                    }
                    byteArrayOutputStream.write(b, 0, i)
                }
            } catch (throwable: Throwable) {
                try {
                    byteArrayOutputStream.close()
                } catch (var7: Throwable) {
                    throwable.addSuppressed(var7)
                }
                throw throwable
            }
            byteArrayOutputStream.close()
            content
        } catch (exception: IOException) {
            exception.printStackTrace()
            null
        }
    }

    @JvmStatic
    fun <T : Keyable<*>?> loadMultiply(mainFile: File?, tClass: Class<T>): List<T> {
        if (mainFile == null) {
            return emptyList()
        }
        for (file in getSubFilesFromFile(mainFile)!!) {
            if (file == null) {
                continue
            }
            val config = loadConfigFile(file)
            for (key in config!!.getKeys(false)) {
                try {
                    val obj = tClass.getDeclaredMethod("load", ConfigurationSection::class.java).invoke(
                        null, config.getConfigurationSection(
                            key!!
                        )
                    )
                    if (Keyable::class.java.isAssignableFrom(obj.javaClass)) {
                        (obj as Keyable<*>).register()
                    }
                } catch (e: NoSuchMethodException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: InvocationTargetException) {
                    e.printStackTrace()
                }
            }
        }
        return emptyList()
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
            wrong("Wrong config!")
            wrong("Cause: " + ColorUtils.unColor(e.cause!!.message.toString()))
        }
        return if (config.getKeys(false).isEmpty()) {
            null
        } else config
    }

    @JvmStatic
    fun getSubFilesFromFile(file: File?): List<File>? {
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
            files.addAll(getSubFilesFromFile(subFile)!!)
        }
        return files
    }

    @JvmStatic
    fun saveResource(resourcePath: String?, replace: Boolean, plugin: Plugin, language: String?) {
        var resourcePath = resourcePath
        if (resourcePath != null && !resourcePath.isEmpty()) {
            resourcePath = (language ?: "") + resourcePath.replace('\\', '/')
            var `in` = plugin.getResource(resourcePath)
            if (`in` == null) {
                val lang: String = Locale.getDefault().toString()
                wrong("The language &b$lang &c doesn't exist!!")
                `in` = plugin.getResource(resourcePath.replace(lang, "en_US"))
            }
            if (resourcePath.contains("languages") && language != null) {
                resourcePath = resourcePath.split(language).toTypedArray()[1]
            }
            val outFile = File(plugin.dataFolder, resourcePath)
            val lastIndex = resourcePath.lastIndexOf(47.toChar())
            val outDir = File(plugin.dataFolder, resourcePath.substring(0, Math.max(lastIndex, 0)))
            if (!outDir.exists()) {
                outDir.mkdirs()
            }
            try {
                if (outFile.exists() && !replace) {
                    plugin.logger.log(
                        Level.WARNING,
                        "Could not save " + outFile.name + " to " + outFile + " because " + outFile.name + " already exists."
                    )
                } else {
                    val out: OutputStream = FileOutputStream(outFile)
                    val buf = ByteArray(1024)
                    var len: Int
                    while (`in`!!.read(buf).also { len = it } > 0) {
                        out.write(buf, 0, len)
                    }
                    out.close()
                    `in`.close()
                }
            } catch (var10: IOException) {
                plugin.logger.log(Level.SEVERE, "Could not save " + outFile.name + " to " + outFile, var10)
            }
        } else {
            throw IllegalArgumentException("ResourcePath cannot be null or empty")
        }
    }

    @JvmStatic
    fun nameNormalize(file: File): String {
        return file.absolutePath.replace(Pouvoir.configManager.serverFile.absolutePath, "").replace("\\", "/")
    }
}