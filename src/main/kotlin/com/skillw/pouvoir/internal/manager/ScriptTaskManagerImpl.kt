package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.script.ScriptTaskManager
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.internal.core.script.common.PouCompiledScript
import com.skillw.pouvoir.internal.core.script.common.pool.TaskPool
import com.skillw.pouvoir.internal.core.script.common.pool.TaskStatus
import java.util.*

object ScriptTaskManagerImpl : ScriptTaskManager() {
    override val key: String = "ScriptTaskManager"
    override val priority: Int = 8
    override val subPouvoir: SubPouvoir
        get() = Pouvoir

    override fun initPool(script: PouCompiledScript) {
        val key = script.key
        remove(key)
        createPool(script).register()
    }

    override val workingTasks: List<String>
        get() {
            val set = LinkedList<String>()
            values.forEach {
                set += it.runningTasks
            }
            return set
        }

    override fun start(
        script: PouCompiledScript,
        function: String,
        arguments: Map<String, Any>,
        vararg parameters: Any?,
    ): TaskStatus? {
        return this[script.key]?.run(function, arguments, *parameters)
    }

    override fun remove(key: String): TaskPool? {
        get(key)?.delete()
        return super.remove(key)
    }

    override fun clear() {
        keys.forEach {
            remove(it)
        }
    }

    private fun createPool(script: PouCompiledScript): TaskPool {
        return TaskPool(script)
    }
}