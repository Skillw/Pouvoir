package com.skillw.pouvoir.manager

import com.skillw.pouvoir.Pouvoir.configManager
import com.skillw.pouvoir.Pouvoir.console
import com.skillw.pouvoir.Pouvoir.scriptEngine
import com.skillw.pouvoir.api.manager.sub.script.CompileManager
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
    override val priority = 6
    override fun compileScript(file: File): CompiledScript? {
        try {
            return (scriptEngine as Compilable).compile(
                InputStreamReader(
                    FileInputStream(file),
                    StandardCharsets.UTF_8
                )
            )
        } catch (e: ScriptException) {
            console.sendLang("script-compile-fail", file.path)
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            console.sendLang("script-file-not-found", file.path)
        }
        return null
    }

    override fun compileScript(path: String): CompiledScript? {
        return compileScript(File(configManager.serverFile, path))
    }

    override val key = "CompileManager"
}