package com.skillw.pouvoir.api.script.engine

import com.skillw.asahi.api.InlineAnalysis
import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.plugin.map.BaseMap
import com.skillw.pouvoir.api.plugin.map.component.Registrable
import com.skillw.pouvoir.api.script.PouFileCompiledScript
import com.skillw.pouvoir.api.script.engine.hook.PouCompiler
import com.skillw.pouvoir.api.script.engine.hook.ScriptBridge
import com.skillw.pouvoir.util.md5
import com.skillw.pouvoir.util.pathNormalize
import com.skillw.pouvoir.util.toStringWithNext
import taboolib.common.util.unsafeLazy
import java.io.File
import java.util.regex.Pattern
import javax.script.Compilable
import javax.script.CompiledScript
import javax.script.ScriptEngine


/**
 * Pou script engine
 *
 * @constructor Create empty Pou script engine
 */
abstract class PouScriptEngine : Registrable<String>, PouCompiler {
    /** Suffixes */
    abstract val suffixes: Array<String>

    /** Function pattern */
    abstract val functionPattern: Pattern

    /** Engine */
    abstract val engine: ScriptEngine

    /** Bridge */
    abstract val bridge: ScriptBridge

    /**
     * Get annotation pattern
     *
     * @return annotation pattern
     */
    open fun getAnnotationPattern(): Pattern = Pattern.compile("//@(?<key>.*)\\((?<args>.*)\\)", 2)

    open fun analysis(string: String, context: AsahiContext = AsahiContext.create()): String {
        return InlineAnalysis.of(string).analysis(context)
    }

    open fun onLoad() {}
    open fun onEnable() {}
    open fun onActive() {}
    open fun onReload() {}
    open fun onDisable() {}


    protected val scriptsCache = BaseMap<String, PouFileCompiledScript>()

    override fun compile(file: File): PouFileCompiledScript? {
        val md5Hex = file.md5() ?: return null
        val normalizePath = file.pathNormalize()
        val md5Script = scriptsCache[normalizePath]
        if (md5Script != null && md5Hex == md5Script.md5) {
            return md5Script
        }
        val scriptLines = file.readLines()
        val script = (engine as Compilable).compile(scriptLines.toStringWithNext())
        return PouFileCompiledScript(
            file,
            md5Hex,
            scriptLines,
            script,
            this
        ).also { scriptsCache[normalizePath] = it }
    }

    protected val evalCache = BaseMap<String, CompiledScript>()
    override fun compile(script: String, vararg params: String): CompiledScript {
        val new = if (script.startsWith(prefix)) script.substring(prefix.length) else script
        return evalCache.map.computeIfAbsent(new) {
            (engine as Compilable).compile(new.trimIndent())
        }
    }

    protected open val prefix by unsafeLazy { "$key::" }
    override fun canCompile(script: String): Boolean {
        return script.startsWith(prefix)
    }

    override fun register() {
        Pouvoir.scriptEngineManager.register(this)
        Pouvoir.compileManager.register(this)
    }
}