package com.skillw.pouvoir.internal.feature.trigger

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.pouvoir.api.feature.trigger.BaseTrigger
import com.skillw.pouvoir.internal.feature.trigger.bukkit.BukkitEventTrigger
import com.skillw.pouvoir.internal.feature.trigger.custom.CustomTrigger

/**
 * @className Util
 *
 * @author Glom
 * @date 2023/1/22 20:21 Copyright 2023 user. All rights reserved.
 */


fun MutableMap<*, *>.loadIn(trigger: BaseTrigger) {
    this as MutableMap<String, Any>
    put("trigger", trigger)
    if (trigger is CustomTrigger) {
        putAll(trigger.data)
        (trigger.data["context"] as? AsahiContext?)?.let {
            putAll(it)
        }
        (trigger.data["origin-trigger"] as? BaseTrigger?)?.let {
            loadIn(it)
        }
    } else if (trigger is BukkitEventTrigger) {
        put("event", trigger.event)
        put("isCancelled", trigger.isCancelled)
    }
}