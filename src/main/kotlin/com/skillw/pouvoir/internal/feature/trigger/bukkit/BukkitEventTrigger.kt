package com.skillw.pouvoir.internal.feature.trigger.bukkit

import com.skillw.pouvoir.api.feature.trigger.BaseTrigger
import org.bukkit.event.Cancellable
import org.bukkit.event.Event

/**
 * @className CustomTrigger
 *
 * @author Glom
 * @date 2023/1/8 19:36 Copyright 2024 Glom.
 */
class BukkitEventTrigger(key: String, val event: Event) : BaseTrigger(key) {
    var isCancelled: Boolean = false
        get() = (event as? Cancellable)?.isCancelled ?: false
        set(value) {
            field = value
            (event as? Cancellable)?.isCancelled = value
        }

}