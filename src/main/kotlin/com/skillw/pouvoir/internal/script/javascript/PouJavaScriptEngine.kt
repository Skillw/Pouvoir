package com.skillw.pouvoir.internal.script.javascript

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import com.skillw.pouvoir.internal.manager.ScriptEngineManagerImpl
import com.skillw.pouvoir.internal.manager.ScriptEngineManagerImpl.addCheckVarsFunc
import com.skillw.pouvoir.internal.script.common.hook.ScriptBridge
import com.skillw.pouvoir.internal.script.common.top.TopLevel.putAllTopLevel
import com.skillw.pouvoir.internal.script.javascript.impl.NashornLegacy
import com.skillw.pouvoir.internal.script.javascript.impl.NashornNew
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import java.util.regex.Pattern
import javax.script.ScriptContext
import javax.script.ScriptEngine

@AutoRegister
@RuntimeDependencies(
    RuntimeDependency(
        "!org.openjdk.nashorn:nashorn-core:15.4",
        test = "!jdk.nashorn.api.scripting.NashornScriptEngineFactory"
    )
)
object PouJavaScriptEngine : PouScriptEngine() {
    override val key: String = "javascript"
    override val suffixes: Array<String> = arrayOf("js")
    override val functionPattern: Pattern = Pattern.compile("^function (?<name>.*)\\(.*\\)(| +)\\{$")
    override val bridge: ScriptBridge by lazy(LazyThreadSafetyMode.NONE) {
        try {
            Class.forName("jdk.nashorn.api.scripting.NashornScriptEngineFactory")
            NashornLegacy
        } catch (ex: ClassNotFoundException) {
            NashornNew
        }
    }


    override val engine: ScriptEngine
        get() = bridge.getEngine("-doe", "--language=es6").apply {
            context.apply {
                addCheckVarsFunc()
                getBindings(ScriptContext.ENGINE_SCOPE).apply {
                    putAll(ScriptEngineManagerImpl.globalVariables)
                    putAllTopLevel()
                }
            }
        }
}