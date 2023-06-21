package com.skillw.pouvoir.internal.feature.listener

import com.skillw.pouvoir.Pouvoir.listenerManager
import com.skillw.pouvoir.api.plugin.map.component.Registrable
import taboolib.common.platform.Platform
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.registerBukkitListener
import java.io.Closeable
import java.util.function.Consumer

class CustomListener<T> private constructor(
    override val key: String,
    val platform: Platform,
    val eventClass: Class<T>,
    val priority: EventPriority,
    val ignoreCancel: Boolean = false,
    val exec: Closeable.(T) -> Unit,
) : Registrable<String> {

    class Builder<T>(
        val key: String,
        val platform: Platform,
        val event: Class<T>,
        val eventPriority: EventPriority = EventPriority.NORMAL,
        val ignoreCancel: Boolean = false,
        val exec: Consumer<T>,
    ) {
        fun build(): CustomListener<T> {
            return CustomListener(key, platform, event, eventPriority, ignoreCancel) {
                exec.accept(it)
            }
        }
    }

    fun unRegister() {
        listenerManager.remove(key)
    }

    override fun register() {
        unRegister()
        listenerManager[key] = registerBukkitListener(eventClass, priority, ignoreCancel, exec)

    }
}