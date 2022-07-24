package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.script.ScriptEngineManager.Companion.compileScript
import com.skillw.pouvoir.api.manager.sub.script.ScriptEngineManager.Companion.searchEngine
import com.skillw.pouvoir.api.manager.sub.script.ScriptManager
import com.skillw.pouvoir.api.script.PouCompiledScript
import com.skillw.pouvoir.internal.manager.PouvoirConfig.debug
import taboolib.common.platform.function.console
import taboolib.common.platform.function.getDataFolder
import taboolib.common5.FileWatcher
import taboolib.module.lang.sendLang
import java.io.File
import java.io.FileNotFoundException
import javax.script.ScriptException

object ScriptManagerImpl : ScriptManager() {
    override val key = "ScriptEngineManager"
    override val priority: Int = 8
    override val subPouvoir = Pouvoir

    private val dirs = HashSet<File>()

    private val watcher by lazy(LazyThreadSafetyMode.NONE) { FileWatcher() }

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
        values.forEach {
            it.remove()
        }
        this.clear()
        dirs.forEach { addScript(it) }
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
        file.listFiles()?.filter {
            it.searchEngine() != null
        }?.forEach {
            addScript(it)
        }
    }

    override fun <T> invoke(pathWithFunction: String, variables: Map<String, Any>, vararg arguments: Any?): T? {
        val splits = pathWithFunction.split("::")
        if (splits.size < 2) {
            console().sendLang("script-invoke-wrong-format", pathWithFunction)
        }
        val path = splits[0]
        val function = splits[1]
        return invoke<T>(path, function, variables, arguments)
    }

    override fun search(path: String): PouCompiledScript? {
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
            console().sendLang("script-file-not-found", key)
        }
        return null
    }

    override fun <T> invoke(path: String, function: String, variables: Map<String, Any>, vararg arguments: Any?): T? {
        val script = search(path) ?: return null
        if (debug) {
            Pouvoir.debug("&eInvoking script &6${script.key} &e's function &d$function")
            Pouvoir.debug("&eVariables: ")
            Pouvoir.debug(
                "&3$variables"
            )
            Pouvoir.debug("&eArguments: ")
            Pouvoir.debug(
                "&3$arguments"
            )
        }
        val start = System.currentTimeMillis()
        val result = try {
            script.invoke(function, variables, arguments)
        } catch (e: ScriptException) {
            console().sendLang("script-invoke-script-exception", function, key)
            e.printStackTrace()
            null
        } catch (e: Exception) {
            console().sendLang("script-invoke-exception", function, key)
            e.printStackTrace()
            null
        }
        val end = System.currentTimeMillis()
        Pouvoir.debug("&ereturn: &6$result &e, in &9${end - start}ms")
        return result as T?
    }

}