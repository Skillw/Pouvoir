package com.skillw.pouvoir.api.function

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.able.Registrable
import java.util.function.Function

/**
 * @className LegacyFunction
 *
 * @author Glom
 * @date 2022/8/4 0:01 Copyright 2022 user. All rights reserved.
 */
abstract class LegacyFunction(
    override val key: String,
) : Registrable<String>,
    Function<Array<String>, Any?> {

    /**
     * Predicate
     *
     * 如果返回true，则执行该函数
     *
     * @param args
     * @return
     */
    protected abstract fun predicate(args: Array<String>): Boolean

    /**
     * Function
     *
     * 执行部分
     *
     * @param args
     * @return
     */
    protected abstract fun function(args: Array<String>): Any?

    override fun register() {
        Pouvoir.inlineFunctionManager.register(this)
    }

    override fun apply(args: Array<String>) = if (predicate(args)) function(args) else null

}