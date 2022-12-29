package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.script.CompileManager
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import com.skillw.pouvoir.internal.core.script.common.PouCompiledScript
import com.skillw.pouvoir.internal.core.script.javascript.PouJavaScriptEngine
import com.skillw.pouvoir.util.FileUtils.listSubFiles
import com.skillw.pouvoir.util.FileUtils.md5
import com.skillw.pouvoir.util.FileUtils.pathNormalize
import com.skillw.pouvoir.util.FileUtils.serverFolder
import com.skillw.pouvoir.util.StringUtils.toStringWithNext
import taboolib.common.platform.function.console
import taboolib.common.platform.function.warning
import taboolib.module.lang.sendLang
import java.io.File
import java.io.FileNotFoundException
import javax.script.Compilable
import javax.script.CompiledScript
import javax.script.ScriptContext.ENGINE_SCOPE
import javax.script.ScriptContext.GLOBAL_SCOPE
import javax.script.ScriptEngine
import javax.script.SimpleBindings

object CompileManagerImpl : CompileManager() {

    override val key: String = "CompileManager"
    override val priority: Int = 7
    override val subPouvoir: SubPouvoir = Pouvoir
    private val scripts = BaseMap<String, PouCompiledScript>()
    private val globalScripts = HashSet<File>()
    private val configScripts = HashSet<File>()
    private val globalMembers = HashMap<String, Any>()
    private fun reloadGlobalScripts() {
        fun addScript(file: File) {
            if (file.extension == "js") {
                addGlobal(file)
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
    }

    override fun onLoad() {
        reloadGlobalScripts()
    }

    override fun onReload() {
        reloadGlobalScripts()
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
        globalMembers.putAll(engine.context.getBindings(ENGINE_SCOPE))
    }

    override fun addGlobal(file: File) {
        if (file.isDirectory) {
            file.listSubFiles().filter { it.extension == "js" }.forEach {
                globalScripts += it
            }
        } else
            globalScripts += file
        updateImports()
    }

    override fun removeGlobal(file: File) {
        if (file.isDirectory) {
            file.listSubFiles().filter { it.extension == "js" }.forEach {
                globalScripts -= it
            }
        } else
            globalScripts -= file
        updateImports()
    }

    private fun CompiledScript.init() {
        engine.importGlobalScripts()
        eval()
        engine.eval(
            """
                        
                        function NeigeNB(){}
                        NeigeNB.prototype = this
                        function $SCRIPT_OBJ(){
                          return new NeigeNB()
                         }
                          """.trimIndent()
        )
    }

    private fun compileToScript(file: File, pouEngine: PouScriptEngine): PouCompiledScript? {
        val md5Hex = file.md5() ?: return null
        val normalizePath = file.pathNormalize()
        val md5Script = scripts[normalizePath]
        if (md5Script != null && md5Hex == md5Script.md5) {
            return md5Script
        }
        val scriptLines = file.readLines()
        val script = (pouEngine.engine as Compilable).compile(scriptLines.toStringWithNext())
            .apply { if (pouEngine.key == ("javascript")) init() }
        return PouCompiledScript(
            file,
            md5Hex,
            scriptLines,
            script,
            pouEngine
        ).also { scripts[normalizePath] = it }
    }

    private val evalCache = BaseMap<Int, CompiledScript>()
    override fun compile(script: String): CompiledScript {
        return evalCache.map.getOrPut(script.hashCode()) {
            (PouJavaScriptEngine.engine as Compilable).compile(("function main(){$script\n}").trimIndent())
                .apply { init() }
        }
    }

    override fun compile(file: File): PouCompiledScript? {
        val suffix = file.extension
        val pouScriptEngine = Pouvoir.scriptEngineManager.getEngine(suffix)
        pouScriptEngine ?: kotlin.run {
            console().sendLang("script-engine-valid-suffix", suffix)
            return null
        }
        val start = System.currentTimeMillis()
        val path = file.pathNormalize()
        console().sendLang("script-compile-start", path)
        val script =
            try {
                compileToScript(file, pouScriptEngine)
            } catch (e: FileNotFoundException) {
                console().sendLang("script-file-not-found", file.path)
                return null
            } catch (e: Exception) {
                console().sendLang("script-compile-fail", file.path)
                e.printStackTrace()
                return null
            }
        val end = System.currentTimeMillis()
        console().sendLang("script-compile-end", path, end - start)
        return script
    }

    private fun ScriptEngine.importGlobalScripts() {
        this.context.setBindings(SimpleBindings().apply { putAll(globalMembers) }, GLOBAL_SCOPE)
    }

}