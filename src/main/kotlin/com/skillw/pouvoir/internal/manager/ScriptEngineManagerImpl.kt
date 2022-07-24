package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.script.ScriptEngineManager
import com.skillw.pouvoir.api.script.PouCompiledScript
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import com.skillw.pouvoir.util.FileUtils.pathNormalize
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import java.io.File
import java.io.FileNotFoundException
import java.util.concurrent.ConcurrentHashMap

object ScriptEngineManagerImpl : ScriptEngineManager() {
    override val key = "ScriptEngineManager"
    override val priority: Int = 5
    override val subPouvoir = Pouvoir
    private val suffixMap = ConcurrentHashMap<String, PouScriptEngine>()
    override val globalVariables: MutableMap<String, Any> = ConcurrentHashMap()


    override fun register(key: String, value: PouScriptEngine) {
        super.register(key, value)
        for (suffix in value.suffixes) {
            suffixMap[suffix] = value
        }
    }


    override fun getEngine(suffix: String): PouScriptEngine? {
        return suffixMap[suffix]
    }

    override fun compile(file: File): PouCompiledScript? {
        val suffix = file.extension
        val pouScriptEngine = Pouvoir.scriptEngineManager.getEngine(suffix)
        pouScriptEngine ?: kotlin.run {
            console().sendLang("script-engine-valid-suffix", suffix)
            return null
        }
        val start = System.currentTimeMillis()
        console().sendLang("script-compile-start", file.pathNormalize())
        val script =
            try {
                pouScriptEngine.compile(file)
            } catch (e: FileNotFoundException) {
                console().sendLang("script-file-not-found", file.path)
                return null
            } catch (e: Exception) {
                console().sendLang("script-compile-fail", file.path)
                e.printStackTrace()
                return null
            }
        val end = System.currentTimeMillis()
        console().sendLang("script-compile-end", end - start)
        return script
    }


}