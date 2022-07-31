package com.skillw.pouvoir.api.manager.sub.script

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import java.io.File

/**
 * Script engine manager
 *
 * @constructor Create empty Script engine manager
 */
abstract class ScriptEngineManager : KeyMap<String, PouScriptEngine>(), Manager {

    /** Global variables */
    abstract val globalVariables: MutableMap<String, Any>

    /**
     * Get engine
     *
     * @param suffix 文件后缀名
     * @return 根据后缀名获取的引擎
     */
    abstract fun getEngine(suffix: String): PouScriptEngine?

    companion object {

        @JvmStatic
        fun File.searchEngine(): PouScriptEngine? {
            return Pouvoir.scriptEngineManager.getEngine(this.extension)
        }
    }
}

