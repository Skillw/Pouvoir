package com.skillw.pouvoir.internal.script.javascript.impl

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.script.CompileManager.Companion.SCRIPT_OBJ
import com.skillw.pouvoir.api.script.ScriptTool.toObject
import com.skillw.pouvoir.internal.script.common.hook.Invoker
import com.skillw.pouvoir.internal.script.common.hook.ScriptBridge
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import jdk.nashorn.api.scripting.NashornScriptEngineFactory
import jdk.nashorn.api.scripting.ScriptObjectMirror
import jdk.nashorn.internal.runtime.ScriptFunction
import jdk.nashorn.internal.runtime.ScriptFunctionData
import java.lang.invoke.MethodType
import java.util.function.Supplier
import javax.script.CompiledScript
import javax.script.Invocable
import javax.script.ScriptEngine

/**
 * @className NashornLegacy
 *
 * @author Glom
 * @date 2022/7/29 21:06 Copyright 2022 user. All rights reserved.
 */
object NashornLegacy : ScriptBridge {
    override fun getEngine(vararg args: String): ScriptEngine =
        NashornScriptEngineFactory().getScriptEngine(args, Pouvoir::class.java.classLoader)

    override fun buildInvoker(script: CompiledScript) = Invoker { function, arguments, parameters, receiver ->
        val engine = script.engine
        script.eval()
        engine.eval(
            """
                        function NeigeNB(){}
                        NeigeNB.prototype = this
                        function $SCRIPT_OBJ(){
                          return new NeigeNB()
                         }
                          """.trimIndent()
        )
        val sObj =
            (engine as Invocable).invokeFunction(SCRIPT_OBJ) as ScriptObjectMirror
        sObj.putAll(arguments)
        receiver(sObj)
        return@Invoker sObj.callMember(function, *parameters)
    }

    override fun toObject(any: Any): Any? {
        val scriptObject = any as? ScriptObjectMirror ?: return null
        if (scriptObject.isFunction) {
            val paramSize = scriptObject.getProperty<ScriptFunction>("sobj")!!
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