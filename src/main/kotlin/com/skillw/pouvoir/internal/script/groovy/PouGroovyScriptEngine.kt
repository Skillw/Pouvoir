package com.skillw.pouvoir.internal.script.groovy

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.api.script.PouCompiledScript
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import com.skillw.pouvoir.util.FileUtils.md5
import com.skillw.pouvoir.util.FileUtils.pathNormalize
import com.skillw.pouvoir.util.StringUtils.toStringWithNext
import org.codehaus.groovy.jsr223.GroovyScriptEngineFactory
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import java.io.File
import java.util.regex.Pattern
import javax.script.Compilable
import javax.script.ScriptEngine
import javax.script.ScriptEngineFactory


@AutoRegister
@RuntimeDependencies(
    RuntimeDependency("org.codehaus.groovy:groovy-jsr223:3.0.11"),
    RuntimeDependency("org.codehaus.groovy:groovy:3.0.11")
)
object PouGroovyScriptEngine : PouScriptEngine() {
    override val key: String = "groovy"
    override val suffixes: Array<String> = arrayOf("groovy")
    private val factory: ScriptEngineFactory by lazy(LazyThreadSafetyMode.NONE) { GroovyScriptEngineFactory() }
    override val functionPattern: Pattern = Pattern.compile("^def (?<name>.*)\\(.*\\)(| +)\\{\$")
    override val engine: ScriptEngine by lazy(LazyThreadSafetyMode.NONE) {
        factory.scriptEngine
    }

    private val scripts = KeyMap<String, PouGroovyScript>()

    override fun compile(file: File): PouCompiledScript? {
        val md5Hex = file.md5() ?: return null
        val normalizePath = file.pathNormalize()
        val md5Script = scripts[normalizePath]
        if (md5Script != null && md5Hex == md5Script.md5) {
            return md5Script
        }
        val scriptLines = file.readLines()
        val script = (engine as Compilable).compile(scriptLines.toStringWithNext())
        return PouGroovyScript(file, scriptLines, md5Hex, script).also { scripts[normalizePath] = it }
    }
}