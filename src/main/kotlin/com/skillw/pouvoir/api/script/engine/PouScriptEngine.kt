package com.skillw.pouvoir.api.script.engine

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.able.Registrable
import com.skillw.pouvoir.api.script.PouCompiledScript
import taboolib.common.platform.function.info
import taboolib.module.chat.colored
import java.io.File
import java.util.regex.Pattern
import javax.script.ScriptContext
import javax.script.ScriptEngine


abstract class PouScriptEngine : Registrable<String> {
    abstract val suffixes: Array<String>
    abstract val functionPattern: Pattern
    abstract val engine: ScriptEngine

    abstract fun compile(file: File): PouCompiledScript?

    open fun getAnnotationPattern(): Pattern = Pattern.compile("//@(?<key>.*)\\((?<args>.*)\\)", 2)

    override fun register() {
        Pouvoir.scriptEngineManager.register(this)
    }

    companion object {
        internal fun ScriptEngine.addCheckVarsFunc(): ScriptEngine {
            put("checkUsableVars", Runnable {
                info("&dUsable Variables:".colored())
                getBindings(ScriptContext.GLOBAL_SCOPE)?.forEach {
                    info("&f${it.key} &7: &6${it.value.javaClass.name} &7= &9${it.value}".colored())
                }
                getBindings(ScriptContext.ENGINE_SCOPE)?.forEach {
                    info("&f${it.key} &7: &6${it.value.javaClass.name} &7= &9${it.value}".colored())
                }
            })
            return this
        }
    }
}