package com.skillw.pouvoir.internal.core.asahi.linking.js

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.script.AsahiCompiledScript
import com.skillw.asahi.util.toLazyMap
import com.skillw.pouvoir.Pouvoir.compileManager
import com.skillw.pouvoir.internal.core.script.javascript.PouJavaScriptEngine
import com.skillw.pouvoir.util.AsahiDataMap
import javax.script.CompiledScript


/**
 * @className ScriptFunctionImpl
 *
 * @author Glom
 * @date 2022/12/28 21:24 Copyright 2022 user. All rights reserved.
 */
class NativeJSFunctionImpl constructor(
    key: String,
    paramNames: Array<String>,
    content: CompiledScript,
    initial: AsahiContext.() -> Unit = {},
) : NativeJSFunction(key, paramNames, content) {

    override val paramCount = paramNames.size
    private val initial: Map<String, Any> = AsahiContext.create().apply(initial)
    override fun invoke(context: AsahiContext, vararg params: Any?): Any? {
        if (params.size < paramCount) {
            throw IllegalArgumentException("Wrong params count! (${params.size} / $paramCount)")
        }
        val invokeContext = context.clone().apply {
            putAll(initial)
        }
        paramNames.forEachIndexed { index, name ->
            invokeContext[name] = params[index] ?: "null"
        }
        invokeContext.run {
            if (containsKey("args")) {
                put("super.args", get("args") ?: emptyArray<Any?>())
            }
            put("args", params)
        }
        return invokeContext.run {
            PouJavaScriptEngine.bridge.invoke(content, "main", mapOf("context" to this), *params)
        }.also {
            invokeContext.forEach { key, value ->
                if (key == "super.args" || key == "args") return@forEach
                if (key.startsWith("@")) return@forEach
                if (key !in paramNames && context.containsKey(key) && !initial.containsKey(key)) context[key] = value
            }
        }
    }

    companion object {
        @JvmStatic
        fun create(
            key: String,
            paramNames: Array<String>,
            content: AsahiCompiledScript,
            initial: AsahiContext.() -> Unit = {},
        ): NativeJSFunction {
            return NativeJSFunctionImpl(key, paramNames, content, initial)
        }

        @JvmStatic
        fun deserialize(key: String, content: String): NativeJSFunction {
            val script = compileManager.compile(content, PouJavaScriptEngine)
            return NativeJSFunctionImpl(key, emptyArray(), script)
        }

        @JvmStatic
        fun deserialize(key: String, map: Map<String, Any>): NativeJSFunction {
            val data = AsahiDataMap().apply { putAll(map) }
            val params = data.get("params", emptyList<String>()).toTypedArray()
            val context = data.get("context", emptyMap<String, Any>()).toLazyMap()
            val script =
                compileManager.compile(data.get("content", "print('Not yet implemented')"), PouJavaScriptEngine)
            return NativeJSFunctionImpl(key, params, script) {
                putAll(context)
            }
        }
    }
}