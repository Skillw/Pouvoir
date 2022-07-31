package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.scriptTaskManager
import com.skillw.pouvoir.api.manager.sub.script.CompileManager
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import com.skillw.pouvoir.internal.script.common.PouCompiledScript
import com.skillw.pouvoir.util.FileUtils.md5
import com.skillw.pouvoir.util.FileUtils.pathNormalize
import com.skillw.pouvoir.util.StringUtils.toStringWithNext
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import java.io.File
import java.io.FileNotFoundException
import javax.script.Compilable

object CompileManagerImpl : CompileManager() {

    override val key: String = "CompileManager"
    override val priority: Int = 7
    override val subPouvoir: SubPouvoir = Pouvoir
    private val scripts = BaseMap<String, PouCompiledScript>()

    private fun compileToScript(file: File, pouEngine: PouScriptEngine): PouCompiledScript? {
        val md5Hex = file.md5() ?: return null
        val normalizePath = file.pathNormalize()
        val md5Script = scripts[normalizePath]
        if (md5Script != null && md5Hex == md5Script.md5) {
            return md5Script
        }
        val scriptLines = file.readLines()
        val script = (pouEngine.engine as Compilable).compile(scriptLines.toStringWithNext())
        return PouCompiledScript(
            file,
            md5Hex,
            scriptLines,
            script,
            pouEngine
        ).also { scriptTaskManager.initPool(it);scripts[normalizePath] = it }
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


}