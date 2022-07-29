package com.skillw.pouvoir.internal.script.common.pool

import com.skillw.pouvoir.api.able.Keyable
import com.skillw.pouvoir.internal.script.common.hook.Invoker
import taboolib.module.chat.TellrawJson
import taboolib.module.chat.colored

/**
 * @className TaskStatus
 * @author Glom
 * @date 2022/7/28 1:48
 * Copyright  2022 user. All rights reserved.
 */
class TaskStatus(override val key: String, private val invoker: Invoker) :
    Keyable<String> {
    private var start: Long = 0
    private var scriptTask: ScriptTask? = null
        @Synchronized
        set
        @Synchronized
        get

    fun info(): TellrawJson? {
        val end = System.currentTimeMillis()
        return if (start != 0L) TellrawJson().append("&e - &6$key &aFunction: &b${scriptTask?.function} &aTotal: &9${end - start}ms".colored())
            .hoverText("&cClick to cancel!".colored())
            .runCommand("/pou engine stop $key") else null
    }

    fun start(
        function: String,
        arguments: Map<String, Any>,
        vararg parameters: Any?,
        receiver: MutableMap<String, Any>.() -> Unit,
    ): ScriptTask {
        return ScriptTask(function) {
            start = System.currentTimeMillis()
            return@ScriptTask invoker.invoke(function, arguments, *parameters, receiver = receiver)
        }.also { scriptTask = it }
    }

    fun stop() {
        start = 0
        scriptTask?.cancel()
        scriptTask = null
    }

    fun isRunning(): Boolean {
        return (scriptTask != null && !scriptTask!!.isCancelled).also { if (!it) stop() }
    }

    fun get(): Any? {
        return scriptTask?.get()
    }

}