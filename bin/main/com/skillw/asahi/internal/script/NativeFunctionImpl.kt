package com.skillw.asahi.internal.script

import com.skillw.asahi.api.AsahiAPI.compile
import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.script.AsahiCompiledScript
import com.skillw.asahi.api.script.linking.NativeFunction
import com.skillw.asahi.util.AsahiDataMap
import com.skillw.asahi.util.toLazyMap


/**
 * @className ScriptFunctionImpl
 *
 * @author Glom
 * @date 2022/12/28 21:24 Copyright 2022 user. All rights reserved.
 */
class NativeFunctionImpl constructor(
    key: String,
    paramNames: Array<String>,
    content: AsahiCompiledScript,
    initial: AsahiContext.() -> Unit = {},
) : NativeFunction(key, paramNames, content) {

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
        return invokeContext.run { content.run() }.also {
            invokeContext.forEach { key, value ->
                if (key == "super.args" || key == "args") return@forEach
                if (key.startsWith("@")) return@forEach
                if (key !in paramNames && context.containsKey(key) && !initial.containsKey(key)) context[key] = value
            }
        }
    }

    companion object {
        
        fun create(
            key: String,
            paramNames: Array<String>,
            content: AsahiCompiledScript,
            initial: AsahiContext.() -> Unit = {},
        ): NativeFunction {
            return NativeFunctionImpl(key, paramNames, content, initial)
        }

        
        fun deserialize(key: String, content: String, vararg namespaces: String): NativeFunction {
            val script = content.compile(*namespaces)
            return NativeFunctionImpl(key, emptyArray(), script)
        }

        
        fun deserialize(key: String, map: Map<String, Any>, vararg namespaces1: String): NativeFunction {
            val data = AsahiDataMap().apply { putAll(map) }
            val params = data.get("params", emptyList<String>()).toTypedArray()
            val namespaces = data.get("namespaces", arrayListOf<String>()).apply {
                addAll(namespaces1)
            }
            val import = data.get("import", emptyList<String>()).toTypedArray()
            val context = data.get("context", emptyMap<String, Any>()).toLazyMap()
            val script = data.get("content", "print 'Not yet implemented'").compile(*namespaces.toTypedArray())
            return NativeFunctionImpl(key, params, script) {
                import(*import)
                putAll(context)
            }
        }
    }
}