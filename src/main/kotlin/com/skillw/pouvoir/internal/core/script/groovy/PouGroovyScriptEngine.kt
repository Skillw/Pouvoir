package com.skillw.pouvoir.internal.core.script.groovy

import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import com.skillw.pouvoir.api.script.engine.hook.ScriptBridge
import com.skillw.pouvoir.internal.core.script.asahi.PouAsahiScriptEngine
import org.codehaus.groovy.jsr223.GroovyScriptEngineFactory
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import java.util.regex.Pattern
import javax.script.Compilable
import javax.script.CompiledScript
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
    override val functionPattern: Pattern = Pattern.compile("^def (?<name>.*)\\(.*\\)(| +)\\{\$")
    override val bridge: ScriptBridge = GroovyBridge

    private val factory: ScriptEngineFactory by lazy { GroovyScriptEngineFactory() }
    override val engine: ScriptEngine
        get() = factory.scriptEngine


    override fun compile(script: String, vararg params: String): CompiledScript {
        val new = if (script.startsWith(prefix)) script.substring(prefix.length) else script
        return evalCache.map.computeIfAbsent(new) {
            (PouAsahiScriptEngine.engine as Compilable).compile("def main(${params.joinToString(",")}) { $new }")
        }
    }
}