package com.skillw.pouvoir.internal.core.script.asahi

import com.skillw.asahi.api.script.AsahiEngine
import com.skillw.pouvoir.api.script.engine.hook.ScriptBridge
import javax.script.CompiledScript
import javax.script.Invocable
import javax.script.ScriptEngine

object AsahiBridge : ScriptBridge {
    override fun getEngine(vararg args: String): ScriptEngine = PouAsahiScriptEngine.engine
    override fun invoke(
        script: CompiledScript,
        function: String,
        arguments: Map<String, Any>,
        vararg parameters: Any?,
    ): Any? {
        (script.engine as AsahiEngine).context().putAll(arguments)
        return (script.engine as Invocable).invokeFunction(
            function,
            *parameters
        )
    }
}