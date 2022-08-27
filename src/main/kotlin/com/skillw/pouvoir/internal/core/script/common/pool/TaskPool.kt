package com.skillw.pouvoir.internal.core.script.common.pool

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.able.Registrable
import com.skillw.pouvoir.internal.core.script.common.PouCompiledScript
import com.skillw.pouvoir.internal.feature.hologram.ConcurrentHashSet
import taboolib.module.chat.TellrawJson
import java.util.function.Supplier
import javax.script.ScriptEngine

/**
 * @className TaskPool
 *
 * @author Glom
 * @date 2022/7/28 1:35 Copyright 2022 user. All rights reserved.
 */
class TaskPool(private val pouCompiledScript: PouCompiledScript) : Registrable<String> {
    override val key: String = pouCompiledScript.key
    private val pool = ConcurrentHashSet<TaskStatus>()
        @Synchronized
        get
    private var deleted = false
    private val pouEngine = pouCompiledScript.pouEngine
    val engine: ScriptEngine = pouCompiledScript.script.engine
    private var count = 1
        get() = field++

    fun info(): List<TellrawJson> {
        return pool.mapNotNull { it.info() }
    }

    fun delete() {
        deleted = true
    }

    fun run(function: String, arguments: Map<String, Any>, vararg parameters: Any?): TaskStatus {
        return TaskStatus(
            "$key-task-$count",
            pouEngine.bridge.buildInvoker(pouCompiledScript.script)
        ).also {
            it.start(function, arguments, *parameters) {
                put("isCancelled", Supplier<Boolean> {
                    return@Supplier !it.isRunning()
                })
            }
            pool.add(it)
        }
    }

    val runningTasks: Set<String>
        get() {
            return pool.filter { it.isRunning() }.map {
                it.key
            }.toSet()
        }

    fun stop(key: String): Boolean {
        return pool.firstOrNull { it.key == key }?.run {
            stop()
            pool.remove(this)
            true
        } ?: false
    }

    override fun register() {
        Pouvoir.scriptTaskManager.register(this)
    }
}