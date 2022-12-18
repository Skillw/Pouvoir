package com.skillw.pouvoir.internal.core.script.javascript.impl

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.script.CompileManager.Companion.SCRIPT_OBJ
import com.skillw.pouvoir.api.script.ScriptTool.toObject
import com.skillw.pouvoir.internal.core.script.common.hook.Invoker
import com.skillw.pouvoir.internal.core.script.common.hook.ScriptBridge
import jdk.nashorn.api.scripting.NashornScriptEngineFactory
import jdk.nashorn.api.scripting.ScriptObjectMirror
import jdk.nashorn.internal.runtime.ScriptFunction
import jdk.nashorn.internal.runtime.ScriptFunctionData
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.invokeMethod
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
        val sObj =
            (engine as Invocable).invokeFunction(SCRIPT_OBJ) as ScriptObjectMirror
        sObj.putAll(arguments)
        receiver(sObj)
        return@Invoker sObj.callMember(function, *parameters)
    }

    override fun toObject(any: Any): Any? {
        val scriptObject = any as? ScriptObjectMirror ?: return any
        if (scriptObject.isFunction) {
            val paramSize = scriptObject.getProperty<ScriptFunction>("sobj")!!
                .getProperty<ScriptFunctionData>("data")!!.getProperty<Int>("arity")
            return when (paramSize) {
                0 -> Supplier<Any?> { return@Supplier scriptObject.invokeMethod<Any?>("call", null, emptyArray<Any>()) }
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