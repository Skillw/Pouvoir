package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.configManager
import com.skillw.pouvoir.api.manager.sub.script.CompileManager
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import javax.script.Compilable
import javax.script.CompiledScript
import javax.script.ScriptException

object CompileManagerImpl : CompileManager {
    override val key = "CompileManager"
    override val priority = 7


    override fun compileFile(file: File): CompiledScript? {
        val suffix = file.extension
        val pouScriptEngine = Pouvoir.scriptEngineManager.getEngine(suffix)
        if (pouScriptEngine == null) {
            console().sendLang("script-engine-valid-suffix", suffix)
            return null
        }
        val scriptEngine = pouScriptEngine.scriptEngine
        try {
            return (scriptEngine as Compilable).compile(
                InputStreamReader(
                    FileInputStream(file),
                    StandardCharsets.UTF_8
                )
            )
        } catch (e: ScriptException) {
            console().sendLang("script-compile-fail", file.path)
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            console().sendLang("script-file-not-found", file.path)
        }
        return null
    }

    override fun compileFile(path: String): CompiledScript? {
        return compileFile(File(configManager.serverFile, path))
    }

    override fun compile(script: String, engine: String): CompiledScript? {
        val pouScriptEngine = Pouvoir.scriptEngineManager[engine]
        if (pouScriptEngine == null) {
            console().sendLang("script-engine-not-found", engine)
            return null
        }
        val scriptEngine = pouScriptEngine.scriptEngine
        var temp = script
        if (!temp.contains("function")) {
            temp = """
                function main(){ $temp
                }
                main()
            """.trimIndent()
        }
        try {
            return (scriptEngine as Compilable).compile(temp)
        } catch (e: ScriptException) {
            console().sendLang("script-compile-fail", "\n$temp\n")
            e.printStackTrace()
        }
        return null
    }

}