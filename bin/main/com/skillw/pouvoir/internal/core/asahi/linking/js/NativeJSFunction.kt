package com.skillw.pouvoir.internal.core.asahi.linking.js

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.script.AsahiCompiledScript
import com.skillw.asahi.api.script.linking.Invoker
import javax.script.CompiledScript

/**
 * Native js function
 *
 * Asahi内 javascript 的函数
 *
 * @constructor Create empty Native function
 * @property key 函数名
 * @property paramNames 参数名
 * @property content 执行内容
 */
abstract class NativeJSFunction(val key: String, val paramNames: Array<String>, val content: CompiledScript) :
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
        return "Asahi NativeKetherFunction $key { Params: ${paramNames.toList()} Content: $content }"
    }

    companion object {
        
        fun create(
            key: String,
            paramNames: Array<String>,
            content: AsahiCompiledScript,
            initial: AsahiContext.() -> Unit = {},
        ): NativeJSFunction {
            return NativeJSFunctionImpl.create(key, paramNames, content, initial)
        }

        
        fun deserialize(key: String, content: String): NativeJSFunction {
            return NativeJSFunctionImpl.deserialize(key, content)
        }

        
        fun deserialize(key: String, map: Map<String, Any>): NativeJSFunction {
            return NativeJSFunctionImpl.deserialize(key, map)
        }
    }

    /** Param count */
    abstract val paramCount: Int
}