package com.skillw.pouvoir.internal.core.script.javascript

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import com.skillw.pouvoir.internal.core.script.common.hook.ScriptBridge
import com.skillw.pouvoir.internal.core.script.javascript.impl.NashornLegacy
import com.skillw.pouvoir.internal.core.script.javascript.impl.NashornNew
import com.skillw.pouvoir.internal.manager.ScriptEngineManagerImpl
import java.util.regex.Pattern
import javax.script.ScriptContext
import javax.script.ScriptEngine

@AutoRegister
object PouJavaScriptEngine : PouScriptEngine() {
    override val key: String = "javascript"
    override val suffixes: Array<String> = arrayOf("js")
    override val functionPattern: Pattern = Pattern.compile("^function (?<name>.*)\\(.*\\)(| +)\\{$")
    private val jdkVersion = System.getProperty("java.specification.version").let {
        when (it) {
            "1.7" -> "7"
            "1.8" -> "8"
            else -> it
        }.toInt()
    }
    override val bridge: ScriptBridge by lazy {
        if (jdkVersion >= 11) NashornNew else NashornLegacy
    }


    override val engine: ScriptEngine
        get() = bridge.getEngine("-doe", "--global-per-engine", "--language=es6").apply {
            context.apply {
                getBindings(ScriptContext.ENGINE_SCOPE).apply {
                    putAll(ScriptEngineManagerImpl.globalVariables)
                }
            }
        }


}