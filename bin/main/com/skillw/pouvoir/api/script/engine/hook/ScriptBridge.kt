package com.skillw.pouvoir.api.script.engine.hook

import javax.script.CompiledScript
import javax.script.ScriptEngine

interface ScriptBridge {
    fun getEngine(vararg args: String): ScriptEngine

    fun invoke(
        script: CompiledScript,
        function: String = "main",
        arguments: Map<String, Any>,
        vararg parameters: Any?,
    ): Any?

}