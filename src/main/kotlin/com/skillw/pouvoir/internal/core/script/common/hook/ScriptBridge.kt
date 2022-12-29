package com.skillw.pouvoir.internal.core.script.common.hook

import javax.script.CompiledScript
import javax.script.ScriptEngine

interface ScriptBridge {
    fun getEngine(vararg args: String): ScriptEngine
    fun invoke(
        script: CompiledScript,
        function: String,
        arguments: Map<String, Any>,
        vararg parameters: Any?,
    ): Any?

}