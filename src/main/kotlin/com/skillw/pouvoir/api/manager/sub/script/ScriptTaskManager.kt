package com.skillw.pouvoir.api.manager.sub.script

import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.internal.script.common.PouCompiledScript
import com.skillw.pouvoir.internal.script.common.pool.TaskPool
import com.skillw.pouvoir.internal.script.common.pool.TaskStatus

/**
 * @className ScriptTaskManager
 * @author Glom
 * @date 2022/7/28 1:44
 * Copyright  2022 user. All rights reserved.
 */
abstract class ScriptTaskManager : Manager, KeyMap<String, TaskPool>() {


    abstract fun initPool(script: PouCompiledScript)
    abstract fun start(
        script: PouCompiledScript,
        function: String = "main",
        arguments: Map<String, Any> = emptyMap(),
        vararg parameters: Any?,
    ): TaskStatus?

    abstract val workingEngines: List<String>
}