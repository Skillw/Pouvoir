package com.skillw.pouvoir.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.configManager
import com.skillw.pouvoir.Pouvoir.plugin
import com.skillw.pouvoir.api.manager.sub.script.ScriptManager
import com.skillw.pouvoir.api.script.CompiledFile
import com.skillw.pouvoir.util.FileUtils
import taboolib.module.lang.sendLang
import java.io.File
import java.util.*
import javax.script.CompiledScript

object ScriptManagerImpl : ScriptManager() {
    override val priority = 7
    override val key = "ScriptManager"
    override fun hasScript(path: String): Boolean {
        return if (hasKey(path)) {
            true
        } else {
            map.filter { e -> e.key.endsWith(path) }.isNotEmpty()
        }
    }

    override fun init() {
        files.add(File(plugin.dataFolder, "scripts"))
        reloadFile()
        for (exec in configManager["script"].getStringList("on-init")) {
            Pouvoir.scriptManager.invokePathWithFunction(exec)
        }
    }

    override fun addScript(file: File) {
        CompiledFile(file).register()
    }

    override fun invoke(
        path: String,
        function: String,
        vararg args: Any,
        argsMap: MutableMap<String, Any>
    ): Any? {
        val compiledFile = get(path) ?: return null
        return compiledFile.invoke(function, argsMap, *args)
    }

    override fun invokePathWithFunction(
        pathWithFunction: String,
        vararg args: Any,
        argsMap: MutableMap<String, Any>
    ): Any? {
        val strings = pathWithFunction.split("::").toTypedArray()
        return if (strings.size == 2) {
            val key = strings[0]
            val function = strings[1]
            this.invoke(key, function, args = args, argsMap = argsMap)
        } else "!wrong-format!"
    }

    override fun search(path: String): Optional<CompiledFile> {
        if (hasKey(path)) {
            return Optional.ofNullable(get(path))
        } else {
            val optional = this.map
                .entries
                .stream()
                .filter { (key, _) ->
                    key.endsWith(path)
                }
                .findFirst()
            if (!optional.isPresent) {
                val compiledScript: CompiledScript? = Pouvoir.compileManager.compileScript(path)
                if (compiledScript != null) {
                    val file = File(configManager.serverFile, path)
                    return if (file.exists() && file.isFile) {
                        val compiledFile = CompiledFile(file)
                        if (compiledFile.canCompiled()) {
                            compiledFile.register()
                            Optional.of(compiledFile)
                        } else Optional.empty()
                    } else {
                        Pouvoir.console.sendLang("script-compile-fail", path)
                        Optional.empty()
                    }
                } else {
                    Pouvoir.console.sendLang("script-compile-fail", path)
                }
            }
            val (_, value) = optional.get()
            return Optional.of(value)
        }
    }

    private fun reloadFile() {
        try {
            this.map.clear()
            for (file in files) {
                FileUtils.listFiles(file).forEach {
                    CompiledFile(it).register()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun reload() {
        reloadFile()
        for (exec in configManager["script"].getStringList("on-reload")) {
            Pouvoir.scriptManager.invokePathWithFunction(exec)
        }
    }


    override fun load() {
        for (exec in configManager["script"].getStringList("on-load")) {
            Pouvoir.scriptManager.invokePathWithFunction(exec)
        }
    }

    override fun enable() {
        for (exec in configManager["script"].getStringList("on-enable")) {
            Pouvoir.scriptManager.invokePathWithFunction(exec)
        }
    }

    override fun active() {
        for (exec in configManager["script"].getStringList("on-active")) {
            Pouvoir.scriptManager.invokePathWithFunction(exec)
        }
    }

    override fun disable() {
        for (exec in configManager["script"].getStringList("on-disable")) {
            Pouvoir.scriptManager.invokePathWithFunction(exec)
        }
    }


}