package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.compileManager
import com.skillw.pouvoir.api.manager.sub.script.ScriptManager
import com.skillw.pouvoir.internal.manager.PouConfig.debug
import com.skillw.pouvoir.internal.script.common.PouCompiledScript
import taboolib.common.platform.function.console
import taboolib.common.platform.function.getDataFolder
import taboolib.module.lang.sendLang
import java.io.File
import java.io.FileNotFoundException
import javax.script.ScriptException

object ScriptManagerImpl : ScriptManager() {
    override val key = "ScriptEngineManager"
    override val priority: Int = 8
    override val subPouvoir = Pouvoir


    override fun onReload() {
        clear()
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
            compileManager.addScript(scriptFile)
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
            Pouvoir.debug { "&eInvoking script &6${script.key} &e's function &d$function" }
            Pouvoir.debug { "&eVariables: " }
            Pouvoir.debug { "&3$arguments" }
            Pouvoir.debug { "&eArguments: " }
            Pouvoir.debug { "&3$parameters" }
        }
        val result = try {
            script.invoke(function, arguments, *parameters)
        } catch (e: ScriptException) {
            console().sendLang("script-invoke-script-exception", function, script.key)
            e.printStackTrace()
            null
        } catch (e: Exception) {
            console().sendLang("script-invoke-exception", function, script.key)
            e.printStackTrace()
            null
        }
        return result as T?
    }

}