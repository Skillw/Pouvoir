package com.skillw.pouvoir.api.function

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.able.Registrable
import java.util.function.Function

/**
 * Pou function
 *
 * @constructor Create empty Pou function
 * @property key 函数键
 */
abstract class PouFunction(
    override val key: String,
) : Registrable<String>,
    Function<Array<String>, Any?> {

    /**
     * Predicate
     *
     * 如果返回true，则执行该函数
     *
     * @param args 参数
     * @return 是否执行
     */
    protected abstract fun predicate(args: Array<String>): Boolean

    /**
     * Function 执行部分
     *
     * @param args 参数
     * @return 执行结果
     */
    protected abstract fun function(args: Array<String>): Any?

    override fun register() {
        Pouvoir.inlineFunctionManager.register(this)
    }

    override fun apply(args: Array<String>) = if (predicate(args)) function(args) else null

}