package com.skillw.pouvoir.internal.core.script.groovy

import com.skillw.pouvoir.internal.core.script.common.hook.ScriptBridge
import javax.script.CompiledScript
import javax.script.Invocable
import javax.script.ScriptContext
import javax.script.ScriptEngine

object GroovyBridge : ScriptBridge {
    override fun getEngine(vararg args: String): ScriptEngine = PouGroovyScriptEngine.engine
    override fun invoke(
        script: CompiledScript,
        function: String,
        arguments: Map<String, Any>,
        vararg parameters: Any?,
    ): Any? {
        script.eval()
        script.engine.context.getBindings(ScriptContext.ENGINE_SCOPE).putAll(arguments)
        return (script.engine as Invocable).invokeFunction(
            function,
            *parameters
        )
    }
}