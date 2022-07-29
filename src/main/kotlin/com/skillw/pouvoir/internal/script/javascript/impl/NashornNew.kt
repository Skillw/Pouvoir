package com.skillw.pouvoir.internal.script.javascript.impl

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.script.CompileManager.Companion.SCRIPT_OBJ
import com.skillw.pouvoir.api.script.ScriptTool.toObject
import com.skillw.pouvoir.internal.script.common.hook.Invoker
import com.skillw.pouvoir.internal.script.common.hook.ScriptBridge
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror
import org.openjdk.nashorn.internal.runtime.ScriptFunction
import org.openjdk.nashorn.internal.runtime.ScriptFunctionData
import taboolib.common.reflect.Reflex.Companion.invokeMethod
import taboolib.library.reflex.Reflex.Companion.getProperty
import java.lang.invoke.MethodType
import java.util.function.Supplier
import javax.script.CompiledScript
import javax.script.Invocable
import javax.script.ScriptEngine

object NashornNew : ScriptBridge {
    override fun getEngine(vararg args: String): ScriptEngine =
        NashornScriptEngineFactory().getScriptEngine(args, Pouvoir::class.java.classLoader)

    override fun buildInvoker(script: CompiledScript) = Invoker { function, arguments, parameters, receiver ->
        val engine = script.engine
        script.eval()
        engine.eval(
            """
                        class NeigeNB(){}
                        NeigeNB.prototype = this
                        function $SCRIPT_OBJ(){
                          return new NeigeNB()
                         }
                          """.trimIndent()
        )
        val sObj = (engine as Invocable).invokeFunction(SCRIPT_OBJ) as ScriptObjectMirror
        sObj.putAll(arguments)
        receiver(sObj)
        return@Invoker sObj.callMember(function, *parameters)
    }

    override fun toObject(any: Any): Any? {
        val scriptObject = any as? ScriptObjectMirror ?: return null
        if (scriptObject.isFunction) {
            val paramSize = scriptObject.getProperty<ScriptFunction>("sobj")!!
                .also { it.keySet().forEach { key -> println("$key = ${it.get(key)} ") } }
                .getProperty<ScriptFunctionData>("data")!!
                .invokeMethod<MethodType>("getGenericType")!!
                .parameterCount() - 2
            return when (paramSize) {
                0 -> Supplier<Any?> { return@Supplier scriptObject.invokeMethod<Any>("call", null) }
                1 -> fun(param: Any?): Any? {
                    return scriptObject.call(null, param)
                }

                else -> fun(params: Array<Any?>): Any? {
                    return scriptObject.call(null, *params)
                }
            }
        }
        if (scriptObject.isEmpty()) return scriptObject
        if (scriptObject.isArray) {
            val list: MutableList<Any?> = ArrayList()
            for ((_, result) in scriptObject) {
                if (result is ScriptObjectMirror) {
                    list.add(result.toObject())
                } else {
                    list.add(result)
                }
            }
            return list.toTypedArray()
        }
        val map: MutableMap<String, Any?> = HashMap()
        for ((key, result) in scriptObject) {
            if (result is ScriptObjectMirror) {
                map[key.toString()] = result.toObject()
            } else {
                map[key.toString()] = result
            }
        }
        return map
    }


}