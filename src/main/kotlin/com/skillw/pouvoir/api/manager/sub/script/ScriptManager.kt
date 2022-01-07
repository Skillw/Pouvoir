package com.skillw.pouvoir.api.manager.sub.script

import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.api.script.CompiledFile
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap

abstract class ScriptManager : KeyMap<String, CompiledFile>(), Manager {
    val files = HashSet<File>()
    abstract fun hasScript(path: String): Boolean
    abstract fun addScript(file: File)
    abstract fun invoke(
        path: String,
        function: String = "main",
        vararg args: Any,
        argsMap: MutableMap<String, Any> = ConcurrentHashMap()
    ): Any?

    abstract fun invokePathWithFunction(
        pathWithFunction: String,
        vararg args: Any,
        argsMap: MutableMap<String, Any> = ConcurrentHashMap()
    ): Any?

    abstract fun search(path: String): Optional<CompiledFile>

    override fun get(key: String): CompiledFile? {
        if (hasKey(key))
            return super.get(key)
        else {
            val optional = search(key)
            if (!optional.isPresent) return null
            val compiledFile = optional.get()
            compiledFile.register()
            return compiledFile
        }
    }
}