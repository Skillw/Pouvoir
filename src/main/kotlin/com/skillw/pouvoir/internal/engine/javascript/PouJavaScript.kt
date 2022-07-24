package com.skillw.pouvoir.internal.engine.javascript

import com.skillw.pouvoir.api.script.PouCompiledScript
import com.skillw.pouvoir.internal.manager.ScriptEngineManagerImpl.globalVariables
import java.io.File
import javax.script.CompiledScript
import javax.script.Invocable
import javax.script.ScriptContext
import javax.script.SimpleScriptContext


// Key = file.pathNormalize();
class PouJavaScript(file: File, val md5: String, val script: CompiledScript) :
    PouCompiledScript(file, PouJavaScriptEngine) {

    override fun invoke(function: String, variables: Map<String, Any>, vararg arguments: Any?): Any? {
        val engine = script.engine
        val context: ScriptContext = SimpleScriptContext()
        context.setBindings(
            engine.createBindings().apply { putAll(variables);putAll(globalVariables) },
            ScriptContext.ENGINE_SCOPE
        )
        script.eval(context)
        engine.context = context
        return (engine as Invocable).invokeFunction(function, *arguments)
    }
}