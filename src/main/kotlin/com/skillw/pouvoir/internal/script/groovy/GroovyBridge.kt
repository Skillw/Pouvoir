package com.skillw.pouvoir.internal.script.groovy

import com.skillw.pouvoir.api.manager.sub.script.CompileManager
import com.skillw.pouvoir.internal.script.common.hook.Invoker
import com.skillw.pouvoir.internal.script.common.hook.ScriptBridge
import groovy.lang.GroovyObject
import javax.script.CompiledScript
import javax.script.Invocable
import javax.script.ScriptEngine

object GroovyBridge : ScriptBridge {
    override fun getEngine(vararg args: String): ScriptEngine = PouGroovyScriptEngine.engine
    override fun buildInvoker(script: CompiledScript) = Invoker { function, arguments, parameters, receiver ->
        val engine = script.engine
        script.eval()
        engine.eval(
            """
            class GlomNB {
              $script
            }
            static def ${CompileManager.SCRIPT_OBJ}() {
              return new GlomNB();
            }
        """.trimIndent()
        )
        val gObj = (engine as Invocable).invokeFunction(CompileManager.SCRIPT_OBJ) as GroovyObject
        arguments.forEach { (key, value) ->
            gObj.setProperty(key, value)
        }
        HashMap<String, Any>().apply(receiver).forEach { (key, value) ->
            gObj.setProperty(key, value)
        }
        return@Invoker gObj.invokeMethod(
            function,
            parameters
        )
    }

    override fun toObject(any: Any): Any? {
        return null
    }
}