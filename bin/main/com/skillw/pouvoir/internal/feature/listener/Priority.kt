package com.skillw.pouvoir.internal.feature.listener

import taboolib.common.platform.event.EventPriority

/**
 * @className Priority
 *
 * @author Glom
 * @date 2022/7/9 7:45 Copyright 2022 user.
 */
class Priority(val level: Int = 0) {

    fun toBukkit(): EventPriority = when (level) {
        1 -> EventPriority.LOWEST
        2 -> EventPriority.LOW
        3 -> EventPriority.NORMAL
        4 -> EventPriority.HIGH
        5 -> EventPriority.HIGHEST
        6 -> EventPriority.MONITOR
        else -> EventPriority.NORMAL
    }
}