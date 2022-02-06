package com.skillw.pouvoir.api.manager.sub.script

import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.api.map.LowerMap
import com.skillw.pouvoir.api.script.CompiledFile
import com.skillw.pouvoir.internal.manager.ScriptManagerImpl
import java.io.File
import java.util.*
import javax.script.CompiledScript
import javax.script.ScriptEngine

abstract class ScriptManager : KeyMap<String, CompiledFile>(), Manager {
    val exec = LowerMap<LinkedList<(() -> Unit)>>()
    abstract fun addDir(file: File)
    abstract fun hasScript(path: String): Boolean
    abstract fun addScript(file: File)
    abstract fun invoke(
        path: String,
        function: String = "main",
        argsMap: MutableMap<String, Any> = HashMap(),
        vararg args: Any
    ): Any?

    abstract fun invokePathWithFunction(
        pathWithFunction: String,
        argsMap: MutableMap<String, Any> = HashMap(),
        vararg args: Any
    ): Any?

    abstract fun search(path: String): Optional<CompiledFile>

    abstract fun recompile(key: String): CompiledFile?

    fun recompile(file: File): CompiledFile? {
        return ScriptManagerImpl.recompile(file.name)
    }

    abstract fun addParams(
        scriptEngine: ScriptEngine,
        argsMap: MutableMap<String, Any> = HashMap()
    ): ScriptEngine

    abstract fun invokeString(
        script: String,
        function: String = "main",
        argsMap: MutableMap<String, Any>,
        vararg args: Any
    ): Any?

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
}