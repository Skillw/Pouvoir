package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.script.CompileManager.Companion.compileScript
import com.skillw.pouvoir.api.manager.sub.script.ScriptEngineManager.Companion.searchEngine
import com.skillw.pouvoir.api.manager.sub.script.ScriptManager
import com.skillw.pouvoir.internal.core.script.common.PouCompiledScript
import com.skillw.pouvoir.internal.core.script.javascript.PouJavaScriptEngine
import com.skillw.pouvoir.internal.manager.PouConfig.debug
import com.skillw.pouvoir.util.FileUtils.listSubFiles
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.function.console
import taboolib.common.platform.function.getDataFolder
import taboolib.common5.FileWatcher
import taboolib.common5.mirrorNow
import taboolib.module.chat.colored
import taboolib.module.lang.asLangText
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
        pathWithFunction: String, arguments: Map<String, Any>,
        sender: ProxyCommandSender, vararg parameters: Any?,
    ): T? {
        val splits = pathWithFunction.split("::")
        if (splits.size < 2) {
            console().sendLang("script-invoke-wrong-format", pathWithFunction)
        }
        val path = splits[0]
        val function = splits[1]
        return invoke<T>(path, function, arguments, sender, *parameters)
    }

    override fun search(
        path: String,
        sender: ProxyCommandSender, silent: Boolean,
    ): PouCompiledScript? {
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
                sender.sendLang("script-file-not-found", path)
        }
        return null
    }

    override fun <T> invoke(
        path: String,
        function: String,
        arguments: Map<String, Any>,
        sender: ProxyCommandSender,
        vararg parameters: Any?,
    ): T? {
        val script = search(path, sender, true) ?: return null
        return invoke(script, function, arguments, sender, *parameters)
    }


    override fun <T> evalJs(
        script: String,
        arguments: Map<String, Any>,
        sender: ProxyCommandSender,
    ): T? {
        val hash = script.hashCode()
        debug {
            sender.apply {
                sendLang("script-invoking-info", hash, "EvalScript")
                sendLang("script-invoking-arguments")
                sendMessage("&3$arguments".colored())
            }
        }
        val start = System.currentTimeMillis()
        val compiledScript = Pouvoir.compileManager.compile(script)
        val result = PouJavaScriptEngine.bridge.invoke(compiledScript, "main", arguments = arguments)
        val end = System.currentTimeMillis()
        debug {
            sender.sendLang(
                "command-script-invoke-end",
                hash,
                result.run { if (this is Unit) console().asLangText("kotlin-unit") else toString() },
                (end - start)
            )
        }
        return result as? T?
    }

    override fun <T> invoke(
        script: PouCompiledScript,
        function: String,
        arguments: Map<String, Any>,
        sender: ProxyCommandSender,
        vararg parameters: Any?,
    ): T? {
        debug {
            sender.apply {
                sendLang("script-invoking-info", script.key, function)
                sendLang("script-invoking-arguments")
                sendMessage("&3$arguments".colored())
                sendLang("script-invoking-parameters")
                sendMessage("&3$parameters".colored())
            }
        }
        val start = System.currentTimeMillis()
        val result = mirrorNow("${script.key}::$function") { script.invoke(function, arguments, *parameters) }
        val end = System.currentTimeMillis()
        debug {
            sender.sendLang(
                "command-script-invoke-end",
                script.key,
                result.run { if (this is Unit) console().asLangText("kotlin-unit") else toString() },
                (end - start)
            )
        }
        return result as? T?
    }

}