package com.skillw.pouvoir.api.manager.sub.script

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.map.KeyMap
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import java.io.File

/**
 * 脚本引擎管理器
 *
 * 主要负责：
 * - 维护脚本引擎
 * - 维护脚本全局遍历
 * - 维护类名重定向
 *
 * @constructor Create empty Script engine manager
 */
abstract class ScriptEngineManager : KeyMap<String, PouScriptEngine>(), Manager {

    /** Global variables */
    abstract val globalVariables: MutableMap<String, Any>

    /**
     * 根据文件后缀名获取Pou的脚本引擎
     *
     * @param suffix 文件后缀名
     * @return 根据后缀名获取的引擎
     */
    abstract fun getEngine(suffix: String): PouScriptEngine?

    /**
     * 导入重定向
     *
     * @param from
     * @param to
     */
    abstract fun relocate(from: String, to: String)

    /**
     * 删除重定向
     *
     * @param from
     */
    abstract fun deleteRelocate(from: String)

    /**
     * 重定向路径
     *
     * @param clazzPath String 原路径
     * @return String 重定向后的路径
     */
    abstract fun relocatePath(clazzPath: String): String

    companion object {
        @JvmStatic
        fun File.searchEngine(): PouScriptEngine? {
            return Pouvoir.scriptEngineManager.getEngine(this.extension)
        }
    }
}

