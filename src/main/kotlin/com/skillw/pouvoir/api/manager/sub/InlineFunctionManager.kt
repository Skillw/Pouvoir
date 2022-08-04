package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.function.LegacyFunction
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.KeyMap


/**
 * Inline function manager
 *
 * @constructor Create empty Inline function manager
 */
abstract class InlineFunctionManager : KeyMap<String, LegacyFunction>(), Manager {
    /**
     * Analysis
     *
     * @param text 待处理文本
     * @return 处理后的文本
     */
    abstract fun legacyAnalysis(text: String): String
}