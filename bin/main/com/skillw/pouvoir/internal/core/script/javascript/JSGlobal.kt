package com.skillw.pouvoir.internal.core.script.javascript

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.internal.manager.PouConfig
import com.skillw.pouvoir.util.listSubFiles
import com.skillw.pouvoir.util.serverFolder
import taboolib.common.platform.function.warning
import java.io.File
import javax.script.ScriptContext
import javax.script.ScriptEngine
import javax.script.SimpleBindings

object JSGlobal {
    private val globalScripts = HashSet<File>()
    private val configScripts = HashSet<File>()
    private val globalMembers = HashMap<String, Any>()
    internal fun reloadGlobalScripts() {
        fun addScript(file: File) {
            if (file.extension == "js") {
                addToGlobal(file)
                configScripts += file
            }
        }

        fun removeScript(file: File) {
            removeGlobal(file)
        }
        configScripts.forEach(::removeScript)
        configScripts.clear()
        val script = PouConfig["script"]
        script.getStringList("global-scripts")
            .ifEmpty { listOf("plugins/Pouvoir/scripts/core/") }
            .forEach { path ->
                val file = File(serverFolder, path)
                if (!file.exists()) {
                    warning("No such file path: $path")
                    return@forEach
                }
                file.let {
                    if (it.isDirectory) it.listSubFiles().forEach(::addScript) else addScript(it)
                }
            }

        Pouvoir.scriptManager.values.forEach {
            it.script.engine.importGlobalScripts()
        }
    }

    private fun updateImports() {
        val builder = StringBuilder()
        val prefix = "load(\""
        val suffix = "\")\n"
        globalScripts.forEach {
            builder.append(prefix + it.path.replace("\\", "/") + suffix)
        }
        val imports = builder.toString()
        val engine = PouJavaScriptEngine.engine
        engine.eval(imports)
        globalMembers.clear()
        globalMembers.putAll(engine.context.getBindings(ScriptContext.ENGINE_SCOPE))
    }

    fun addToGlobal(file: File) {
        if (file.isDirectory) {
            file.listSubFiles().filter { it.extension == "js" }.forEach {
                globalScripts += it
            }
        } else
            globalScripts += file
        updateImports()
    }

    fun removeGlobal(file: File) {
        if (file.isDirectory) {
            file.listSubFiles().filter { it.extension == "js" }.forEach {
                globalScripts -= it
            }
        } else
            globalScripts -= file
        updateImports()
    }


    internal fun ScriptEngine.importGlobalScripts() {
        this.context.setBindings(SimpleBindings().apply { putAll(globalMembers) }, ScriptContext.GLOBAL_SCOPE)
    }
}