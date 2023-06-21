package com.skillw.asahi.api

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.script.AsahiCompiledScript
import com.skillw.asahi.api.script.AsahiEngine
import com.skillw.asahi.api.script.AsahiEngineFactory

@Suppress("UNCHECKED_CAST")
object AsahiAPI {

    /**
     * 解析文本中的Asahi 例如:
     * - "测试Asahi: {random 0 to 1}" 返回: "测试Asahi: 0.88"
     *
     * @param context 上下文
     * @return String 解析后的文本
     * @receiver String 含Asahi的文本
     */
    @JvmStatic
    fun String.analysis(context: AsahiContext = AsahiContext.create(), vararg namespaces: String): String {
        return InlineAnalysis.of(this).addNamespaces(*namespaces).analysis(context)
    }

    /**
     * 执行一段Asahi脚本
     *
     * @param context 上下文
     * @param namespaces 命名空间(含哪些空间的函数)
     * @return Any 结果
     * @receiver String Asahi
     */
    @JvmStatic
    fun String.asahi(
        context: AsahiContext = AsahiContext.create(),
        vararg namespaces: String,
    ): Any {
        return context.run {
            compile(*namespaces).run() ?: "null"
        }
    }

    private val default = AsahiEngineFactory().scriptEngine

    /**
     * 编译脚本
     *
     * @param namespaces Array<out String> 命名空间
     * @param engine AsahiEngine 引擎
     * @return AsahiCompiledScript 预编译脚本
     * @receiver String
     */
    @JvmStatic
    fun String.compile(vararg namespaces: String, engine: AsahiEngine = default): AsahiCompiledScript {
        return engine.compile(this, *namespaces)
    }

    @JvmStatic
    fun Collection<String>.compile(vararg namespaces: String, engine: AsahiEngine = default): AsahiCompiledScript {
        return engine.compile(this, *namespaces)
    }

}