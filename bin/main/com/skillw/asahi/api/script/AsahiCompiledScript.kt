package com.skillw.asahi.api.script

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.quest.Quester
import javax.script.CompiledScript

/**
 * Asahi compiled script
 *
 * Asahi预编译脚本
 *
 * @constructor Create empty Asahi compiled script
 * @property engine
 */
abstract class AsahiCompiledScript(val engine: AsahiEngine) : CompiledScript(), Quester<Any?> {
    /**
     * 回调函数-当前是否已退出执行
     *
     * @param isExit 回调函数
     * @return
     * @receiver
     */
    abstract fun isExit(isExit: AsahiContext.() -> Boolean = { isExit() }): AsahiCompiledScript

    /**
     * 原脚本
     *
     * @return 原脚本
     */
    abstract fun rawScript(): String

    @Throws(AsahiScriptException::class)
    abstract override fun AsahiContext.execute(): Any?

    /**
     * 添加Quester
     *
     * @param quester
     */
    abstract fun add(quester: Quester<Any?>)
}