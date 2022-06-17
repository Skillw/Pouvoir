package com.skillw.pouvoir.util

import java.io.File
import java.net.URISyntaxException
import java.util.jar.JarEntry
import java.util.jar.JarFile

object PluginUtils {
    @JvmStatic
    fun getClasses(plugin: org.bukkit.plugin.Plugin): List<Class<*>> {
        val classes: MutableList<Class<*>> = ArrayList()
        val url = plugin.javaClass.protectionDomain.codeSource.location
        try {
            val src: File = try {
                File(url.toURI())
            } catch (e: URISyntaxException) {
                File(url.path)
            }
            JarFile(src).stream().filter { entry: JarEntry ->
                entry.name.endsWith(".class")
            }.forEach { entry: JarEntry ->
                val className =
                    entry.name.replace('/', '.').substring(0, entry.name.length - 6)
                try {
                    classes.add(Class.forName(className, false, plugin.javaClass.classLoader))
                } catch (ignored: Throwable) {
                }
            }
        } catch (ignored: Throwable) {
        }
        return classes
    }

}