package com.skillw.pouvoir.internal.core.script.asahi

import com.skillw.asahi.api.script.AsahiEngineFactory
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.script.PouFileCompiledScript
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import com.skillw.pouvoir.api.script.engine.hook.ScriptBridge
import com.skillw.pouvoir.util.md5
import com.skillw.pouvoir.util.pathNormalize
import com.skillw.pouvoir.util.toStringWithNext
import java.io.File
import java.util.regex.Pattern
import javax.script.Compilable
import javax.script.ScriptEngine
import javax.script.ScriptEngineFactory


@AutoRegister
object PouAsahiScriptEngine : PouScriptEngine() {
    override val key: String = "asahi"
    override val suffixes: Array<String> = arrayOf("asahi")
    override val functionPattern: Pattern =
        Pattern.compile("^(fun|def).*?(?<name>[a-zA-Z_\$]+).*?\\((?<params>.*?)\\).*?")
    override val bridge: ScriptBridge = AsahiBridge

    override fun getAnnotationPattern(): Pattern {
        return Pattern.compile("#@(?<key>.*)\\((?<args>.*)\\)", 2)
    }

    private val factory: ScriptEngineFactory by lazy(LazyThreadSafetyMode.NONE) { AsahiEngineFactory() }
    override val engine: ScriptEngine
        get() = factory.scriptEngine

    override fun compile(file: File): PouFileCompiledScript? {
        val md5Hex = file.md5() ?: return null
        val normalizePath = file.pathNormalize()
        val md5Script = scriptsCache[normalizePath]
        if (md5Script != null && md5Hex == md5Script.md5) {
            return md5Script
        }
        val scriptLines = file.readLines()
        val script = (engine as Compilable).compile(scriptLines.toStringWithNext()).apply { eval() }
        return PouFileCompiledScript(
            file,
            md5Hex,
            scriptLines,
            script,
            this
        ).also { scriptsCache[normalizePath] = it }
    }
}