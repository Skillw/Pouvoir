package com.skillw.pouvoir.api.script.engine

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.able.Keyable
import com.skillw.pouvoir.api.script.CompiledFile
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import java.util.*
import javax.script.ScriptEngine


open class PouScriptEngine(
    override val key: String,
    scriptEngine: ScriptEngine,
    val functions: (Pair<CompiledFile, List<String>>) -> MutableMap<String, LinkedList<ScriptAnnotationData>>,
    vararg val suffixes: String
) : Keyable<String> {
    private val originScriptEngine: ScriptEngine = scriptEngine

    val scriptEngine: ScriptEngine
        get() = Pouvoir.scriptManager.addStatic(originScriptEngine)

    override fun register() {
        Pouvoir.scriptEngineManager.register(this)
    }
}