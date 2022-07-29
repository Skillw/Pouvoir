package com.skillw.pouvoir.api.script.engine

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.able.Registrable
import com.skillw.pouvoir.internal.script.common.hook.ScriptBridge
import java.util.regex.Pattern
import javax.script.ScriptEngine


abstract class PouScriptEngine : Registrable<String> {
    abstract val suffixes: Array<String>
    abstract val functionPattern: Pattern
    abstract val engine: ScriptEngine


    open fun getAnnotationPattern(): Pattern = Pattern.compile("//@(?<key>.*)\\((?<args>.*)\\)", 2)

    override fun register() {
        Pouvoir.scriptEngineManager.register(this)
    }

    abstract val bridge: ScriptBridge
}