package com.skillw.pouvoir.internal.script.common.pool

import com.skillw.pouvoir.Pouvoir
import java.util.concurrent.Callable
import java.util.concurrent.Future

/**
 * @className ScriptTask
 * @author Glom
 * @date 2022/7/29 20:47
 * Copyright  2022 user. All rights reserved.
 */
class ScriptTask(val function: String, callable: Callable<Any?>) {
    private val task: Future<Any?>

    init {
        task = Pouvoir.poolExecutor.submit(callable)
    }

    var isCancelled = false
    fun cancel() {
        isCancelled = true
        task.cancel(true)
    }

    /*
    InterruptedException CancellationException ScriptException
    (逃)
     */
    fun get(): Any? = task.get()
}