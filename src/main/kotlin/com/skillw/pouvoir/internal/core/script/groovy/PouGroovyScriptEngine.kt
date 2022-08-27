package com.skillw.pouvoir.internal.core.script.groovy

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import com.skillw.pouvoir.internal.core.script.common.hook.ScriptBridge
import org.codehaus.groovy.jsr223.GroovyScriptEngineFactory
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import java.util.regex.Pattern
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

    private val factory: ScriptEngineFactory by lazy(LazyThreadSafetyMode.NONE) { GroovyScriptEngineFactory() }
    override val engine: ScriptEngine
        get() = factory.scriptEngine
}