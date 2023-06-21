package com.skillw.pouvoir.internal.core.script.javascript.impl

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.script.engine.hook.ScriptBridge
import com.skillw.pouvoir.internal.core.script.javascript.PouJavaScriptEngine.SCRIPT_OBJ
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror
import javax.script.CompiledScript
import javax.script.Invocable
import javax.script.ScriptEngine


object NashornNew : ScriptBridge {
    override fun getEngine(vararg args: String): ScriptEngine =
        NashornScriptEngineFactory().getScriptEngine(args, Pouvoir::class.java.classLoader)

    override fun invoke(
        script: CompiledScript,
        function: String,
        arguments: Map<String, Any>,
        vararg parameters: Any?,
    ): Any? {
        val engine = script.engine
        val sObj = (engine as Invocable).invokeFunction(SCRIPT_OBJ) as ScriptObjectMirror
        sObj.putAll(arguments)
        return sObj.callMember(function, *parameters)
    }


}