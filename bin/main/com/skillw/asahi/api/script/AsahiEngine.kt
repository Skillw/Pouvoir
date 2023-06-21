package com.skillw.asahi.api.script

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.internal.context.AsahiContextImpl
import com.skillw.asahi.internal.script.AsahiEngineImpl
import java.io.File
import javax.script.AbstractScriptEngine
import javax.script.Compilable
import javax.script.Invocable

/**
 * Asahi 脚本引擎
 *
 * @param context 上下文
 * @constructor
 */
abstract class AsahiEngine(context: AsahiContext) : AbstractScriptEngine(context), Compilable, Invocable {
    companion object {
        @JvmStatic
        fun create(
            factory: AsahiEngineFactory,
            context: AsahiContext = AsahiContextImpl.create(),
        ): AsahiEngine {
            return AsahiEngineImpl.create(factory, context)
        }
    }

    /**
     * 编译
     *
     * @param file 文件
     * @param namespaces 命名空间
     * @return 预编译脚本
     */
    abstract fun compile(file: File, vararg namespaces: String): AsahiCompiledScript

    abstract override fun compile(script: String): AsahiCompiledScript

    /**
     * 编译
     *
     * @param script 脚本字符串
     * @param namespaces 命名空间
     * @return 预编译脚本
     */
    abstract fun compile(script: String, vararg namespaces: String): AsahiCompiledScript

    /**
     * 编译
     *
     * @param tokens Tokens列表
     * @param namespaces 命名空间
     * @return 预编译脚本
     */
    abstract fun compile(tokens: Collection<String>, vararg namespaces: String): AsahiCompiledScript

    /**
     * 上下文
     *
     * @return
     */
    abstract fun context(): AsahiContext

    /**
     * 全局上下文
     *
     * @return
     */
    abstract fun global(): AsahiContext
}