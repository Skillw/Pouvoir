package com.skillw.pouvoir.api.listener

import com.skillw.pouvoir.Pouvoir.listenerManager
import com.skillw.pouvoir.api.able.Keyable
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.unregisterListener
import java.io.Closeable
import java.util.function.Consumer

class ScriptListener<T> private constructor(
    override val key: String,
    val eventClass: Class<T>,
    val priority: EventPriority,
    val ignoreCancel: Boolean = false,
    val exec: Closeable.(T) -> Unit
) : Keyable<String> {

    companion object {
        fun <T> build(
            key: String,
            event: Class<T>,
            eventPriority: EventPriority = EventPriority.NORMAL,
            ignoreCancel: Boolean = false,
            exec: Consumer<T>
        ): ScriptListener<T> {
            return ScriptListener(key, event, eventPriority, ignoreCancel) {
                exec.accept(it)
            }
        }
    }

    fun unRegister() {
        if (listenerManager.containsKey(key)) {
            unregisterListener(listenerManager[key]!!)
        }
    }

    override fun register() {
        if (listenerManager.containsKey(key)) {
            unregisterListener(listenerManager[key]!!)
        }
        listenerManager[key] = registerBukkitListener(eventClass, priority, ignoreCancel, exec)
    }
}