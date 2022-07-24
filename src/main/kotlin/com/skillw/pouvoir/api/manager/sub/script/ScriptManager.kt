package com.skillw.pouvoir.api.manager.sub.script

import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.api.script.PouCompiledScript
import java.io.File

abstract class ScriptManager : Manager, KeyMap<String, PouCompiledScript>() {

    abstract fun addScript(file: File)
    abstract fun addScriptDir(file: File)

    abstract fun search(path: String): PouCompiledScript?
    abstract fun <T> invoke(
        pathWithFunction: String,
        variables: Map<String, Any> = emptyMap(),
        vararg arguments: Any?
    ): T?

    abstract fun <T> invoke(
        path: String,
        function: String = "main",
        variables: Map<String, Any> = emptyMap(),
        vararg arguments: Any?
    ): T?
}