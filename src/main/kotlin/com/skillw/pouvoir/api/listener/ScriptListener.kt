package com.skillw.pouvoir.api.listener

import com.skillw.pouvoir.Pouvoir.listenerManager
import com.skillw.pouvoir.api.able.Keyable
import taboolib.common.platform.Platform
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.registerBungeeListener
import taboolib.common.platform.function.registerSpongeListener
import taboolib.common.platform.function.unregisterListener
import java.io.Closeable
import java.util.function.Consumer

class ScriptListener<T> private constructor(
    override val key: String,
    val platform: Platform,
    val eventClass: Class<T>,
    val priority: Priority,
    val ignoreCancel: Boolean = false,
    val exec: Closeable.(T) -> Unit
) : Keyable<String> {

    class Builder<T>(
        val key: String,
        val platform: Platform,
        val event: Class<T>,
        val eventPriority: Priority = Priority(0),
        val ignoreCancel: Boolean = false,
        val exec: Consumer<T>
    ) {
        fun build(): ScriptListener<T> {
            return ScriptListener(key, platform, event, eventPriority, ignoreCancel) {
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
        listenerManager[key] = when (platform) {
            Platform.BUKKIT -> registerBukkitListener(eventClass, priority.toBukkit(), ignoreCancel, exec)
            Platform.SPONGE_API_9,
            Platform.SPONGE_API_8,
            Platform.SPONGE_API_7 -> registerSpongeListener(eventClass, priority.toSponge(), ignoreCancel, exec)
            Platform.BUNGEE -> registerBungeeListener(eventClass, priority.level, ignoreCancel, exec)
            else -> registerBukkitListener(eventClass, priority.toBukkit(), ignoreCancel, exec)
        }

    }
}