package com.skillw.pouvoir.internal.core.script.groovy

import com.skillw.pouvoir.internal.core.script.common.hook.Invoker
import com.skillw.pouvoir.internal.core.script.common.hook.ScriptBridge
import javax.script.CompiledScript
import javax.script.Invocable
import javax.script.ScriptContext
import javax.script.ScriptEngine

object GroovyBridge : ScriptBridge {
    override fun getEngine(vararg args: String): ScriptEngine = PouGroovyScriptEngine.engine
    override fun buildInvoker(script: CompiledScript): Invoker {
        return Invoker { function, arguments, parameters, receiver ->
            script.eval()
            script.engine.context.getBindings(ScriptContext.ENGINE_SCOPE).putAll(arguments)
            return@Invoker (script.engine as Invocable).invokeFunction(
                function,
                *parameters
            )
        }
    }

    override fun toObject(any: Any): Any? {
        return null
    }
}