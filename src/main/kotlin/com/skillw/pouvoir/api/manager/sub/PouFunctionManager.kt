package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.Context
import com.skillw.pouvoir.api.function.reader.IReader
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.internal.function.context.SimpleContext


/**
 * Inline function manager
 *
 * @constructor Create empty Inline function manager
 */
abstract class PouFunctionManager : KeyMap<String, PouFunction<*>>(), Manager {
    /**
     * Analysis
     *
     * @param text 待处理文本
     * @return 处理后的文本
     */
    abstract fun parse(text: String): String
    abstract val namespaces: BaseMap<String, HashSet<PouFunction<*>>>
    abstract fun parse(
        string: String,
        namespaces: Array<String> = arrayOf("common"),
        receiver: Context.() -> Unit = {},
    ): Any?

    abstract fun parse(
        reader: IReader,
        namespaces: Array<String> = arrayOf("common"),
        receiver: Context.() -> Unit = {},
    ): Any?


    abstract fun parse(
        reader: IReader,
        namespaces: Array<String> = arrayOf("common"),
        context: Context = SimpleContext(),
    ): Any?
}