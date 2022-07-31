package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.KeyMap


/**
 * Inline function manager
 *
 * @constructor Create empty Inline function manager
 */
abstract class InlineFunctionManager : KeyMap<String, PouFunction>(), Manager {
    /**
     * Analysis
     *
     * @param text 待处理文本
     * @return 处理后的文本
     */
    abstract fun analysis(text: String): String
}