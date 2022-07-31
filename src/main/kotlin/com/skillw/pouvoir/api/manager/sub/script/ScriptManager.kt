package com.skillw.pouvoir.api.manager.sub.script

import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.internal.script.common.PouCompiledScript
import java.io.File

abstract class ScriptManager : Manager, KeyMap<String, PouCompiledScript>() {
    abstract fun addScript(file: File)
    abstract fun addScriptDir(file: File)
    abstract fun search(path: String, silent: Boolean = false): PouCompiledScript?
    abstract fun <T> invoke(
        pathWithFunction: String,
        arguments: Map<String, Any> = emptyMap(),
        vararg parameters: Any?,
    ): T?

    abstract fun <T> invoke(
        path: String,
        function: String = "main",
        arguments: Map<String, Any> = emptyMap(),
        vararg parameters: Any?,
    ): T?

    abstract fun <T> invoke(
        script: PouCompiledScript,
        function: String = "main",
        arguments: Map<String, Any> = emptyMap(),
        vararg parameters: Any?,
    ): T?
}