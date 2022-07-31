package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.script.CompileManager.Companion.compileScript
import com.skillw.pouvoir.api.manager.sub.script.ScriptEngineManager.Companion.searchEngine
import com.skillw.pouvoir.api.manager.sub.script.ScriptManager
import com.skillw.pouvoir.internal.manager.PouConfig.debug
import com.skillw.pouvoir.internal.script.common.PouCompiledScript
import com.skillw.pouvoir.util.FileUtils.listSubFiles
import taboolib.common.platform.function.console
import taboolib.common.platform.function.getDataFolder
import taboolib.common5.FileWatcher
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import java.io.File
import java.io.FileNotFoundException

object ScriptManagerImpl : ScriptManager() {
    override val key = "ScriptManager"
    override val priority: Int = 8
    override val subPouvoir = Pouvoir


    private val dirs = HashSet<File>()

    private val watcher by lazy {
        dirs.runCatching { }
        FileWatcher()
    }

    override fun onLoad() {
        dirs += File(getDataFolder(), "scripts")
    }

    override fun onEnable() {
        reloadScripts()
    }

    override fun onReload() {
        reloadScripts()
    }

    private fun reloadScripts() {
        clear();dirs.forEach { addScript(it) }
    }

    override fun addScript(file: File) {
        if (file.isDirectory) {
            addScriptDir(file)
            return
        }
        file.compileScript()?.apply { register() }
        if (watcher.hasListener(file)) return
        watcher.addSimpleListener(file) {
            addScript(file)
        }
    }

    override fun addScriptDir(file: File) {
        dirs.add(file)
        file.listSubFiles().filter {
            it.searchEngine() != null
        }.forEach {
            addScript(it)
        }
    }

    override fun clear() {
        Pouvoir.scriptManager.values.forEach {
            it.delete()
        }
        super.clear()
    }

    override fun <T> invoke(
        pathWithFunction: String, arguments: Map<String, Any>, vararg parameters: Any?,
    ): T? {
        val splits = pathWithFunction.split("::")
        if (splits.size < 2) {
            console().sendLang("script-invoke-wrong-format", pathWithFunction)
        }
        val path = splits[0]
        val function = splits[1]
        return invoke<T>(path, function, arguments, *parameters)
    }

    override fun search(path: String, silent: Boolean): PouCompiledScript? {
        try {
            if (containsKey(path)) return this[path]
            this.keys
                .firstOrNull { it.endsWith(path) }
                ?.let { script ->
                    return this[script]
                }
            val scriptFile = File(getDataFolder(), path)
            if (!scriptFile.exists() || scriptFile.isDirectory) throw FileNotFoundException()
            addScript(scriptFile)
            return this[path]
        } catch (e: FileNotFoundException) {
            if (!silent)
                console().sendLang("script-file-not-found", path)
        }
        return null
    }

    override fun <T> invoke(
        path: String,
        function: String,
        arguments: Map<String, Any>,
        vararg parameters: Any?,
    ): T? {
        val script = search(path) ?: return null
        return invoke(script, function, arguments, *parameters)
    }

    override fun <T> invoke(
        script: PouCompiledScript,
        function: String,
        arguments: Map<String, Any>,
        vararg parameters: Any?,
    ): T? {
        if (debug) {
            debug { console().sendLang("script-invoking-info", script.key, function) }
            debug { console().sendLang("script-invoking-arguments") }
            debug { console().sendMessage("&3$arguments".colored()) }
            debug { console().sendLang("script-invoking-parameters") }
            debug { console().sendMessage("&3$parameters".colored()) }
        }
        val result = script.invoke(function, arguments, *parameters)
        return result as T?
    }

}