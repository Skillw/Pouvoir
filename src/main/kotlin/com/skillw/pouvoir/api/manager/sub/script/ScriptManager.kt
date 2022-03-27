package com.skillw.pouvoir.api.manager.sub.script

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.script.CompiledFile
import java.io.File
import java.util.*
import javax.script.CompiledScript
import javax.script.ScriptEngine

abstract class ScriptManager : KeyMap<String, CompiledFile>(), Manager {
    abstract fun addDir(file: File, subPouvoir: SubPouvoir)
    abstract fun hasScript(path: String): Boolean
    abstract fun addScript(file: File, subPouvoir: SubPouvoir)
    abstract fun invoke(
        path: String,
        function: String = "main",
        argsMap: MutableMap<String, Any> = HashMap(),
        vararg args: Any
    ): Any?

    abstract fun invoke(
        string: String,
        argsMap: MutableMap<String, Any> = HashMap()
    ): Any?


    abstract fun invokePathWithFunction(
        pathWithFunction: String,
        argsMap: MutableMap<String, Any> = HashMap(),
        vararg args: Any
    ): Any?

    abstract fun search(path: String, subPouvoir: SubPouvoir = Pouvoir): Optional<CompiledFile>

    abstract fun addParams(
        scriptEngine: ScriptEngine,
        argsMap: MutableMap<String, Any> = HashMap()
    ): ScriptEngine

    abstract fun addStatic(scriptEngine: ScriptEngine): ScriptEngine
    abstract fun invoke(
        script: CompiledScript,
        function: String = "main",
        argsMap: MutableMap<String, Any> = HashMap(),
        key: String,
        vararg args: Any
    ): Any?

    abstract fun evalString(
        script: String,
        type: String = "javascript",
        argsMap: MutableMap<String, Any> = HashMap()
    ): Any?

    abstract fun evalString(script: String, argsMap: MutableMap<String, Any>): Any?
    abstract fun reloadDir(file: File, subPouvoir: SubPouvoir)
    abstract fun relocate(script: String): String
    abstract fun evalStringQuickly(script: String, type: String, argsMap: MutableMap<String, Any>): Any?
}