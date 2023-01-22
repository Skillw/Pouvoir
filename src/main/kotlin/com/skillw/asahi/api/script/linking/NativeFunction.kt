package com.skillw.asahi.api.script.linking

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.script.AsahiCompiledScript
import com.skillw.asahi.internal.script.NativeFunctionImpl

/**
 * Native function
 *
 * Asahi内的函数
 *
 * @constructor Create empty Native function
 * @property key 函数名
 * @property paramNames 参数名
 * @property content 执行内容
 */
abstract class NativeFunction(val key: String, val paramNames: Array<String>, val content: AsahiCompiledScript) :
    Invoker {
    /**
     * 调用此函数
     *
     * @param context 上下文
     * @param params 参数
     * @return 结果
     */
    abstract override fun invoke(context: AsahiContext, vararg params: Any?): Any?

    override fun toString(): String {
        return "Asahi NativeFunction $key { Params: ${paramNames.toList()} Content: ${content.rawScript()} }"
    }

    companion object {
        @JvmStatic
        fun create(
            key: String,
            paramNames: Array<String>,
            content: AsahiCompiledScript,
            initial: AsahiContext.() -> Unit = {},
        ): NativeFunction {
            return NativeFunctionImpl.create(key, paramNames, content, initial)
        }

        @JvmStatic
        fun deserialize(key: String, content: String, vararg namespaces: String): NativeFunction {
            return NativeFunctionImpl.deserialize(key, content, *namespaces)
        }

        @JvmStatic
        fun deserialize(key: String, map: Map<String, Any>, vararg namespaces: String): NativeFunction {
            return NativeFunctionImpl.deserialize(key, map, *namespaces)
        }
    }

    /** Param count */
    abstract val paramCount: Int
}