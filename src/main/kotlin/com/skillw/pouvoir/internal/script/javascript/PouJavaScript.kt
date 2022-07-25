package com.skillw.pouvoir.internal.script.javascript

import com.skillw.pouvoir.api.script.PouCompiledScript
import com.skillw.pouvoir.api.script.engine.PouScriptEngine.Companion.addCheckVarsFunc
import com.skillw.pouvoir.internal.manager.ScriptEngineManagerImpl.globalVariables
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import java.io.File
import javax.script.CompiledScript
import javax.script.Invocable
import javax.script.ScriptContext
import javax.script.ScriptContext.ENGINE_SCOPE
import javax.script.SimpleScriptContext


// Key = file.pathNormalize();
class PouJavaScript(file: File, scripts: List<String>, val md5: String, val script: CompiledScript) :
    PouCompiledScript(file, scripts, PouJavaScriptEngine) {

    override fun invoke(function: String, variables: Map<String, Any>, vararg arguments: Any?): Any? {
        val engine = script.engine.addCheckVarsFunc()
        val bindings = engine.createBindings().apply {
            putAll(variables);putAll(globalVariables);
        }
        val context: ScriptContext = SimpleScriptContext().apply { setBindings(bindings, ENGINE_SCOPE) }
        script.eval(context)
        engine.context = context
        return (engine as Invocable).invokeMethod<Any?>("invokeFunction", function, arguments)
    }
}