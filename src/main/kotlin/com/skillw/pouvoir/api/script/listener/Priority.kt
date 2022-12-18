package com.skillw.pouvoir.api.script.listener

import taboolib.common.platform.event.EventOrder
import taboolib.common.platform.event.EventPriority

/**
 * @className Priority
 *
 * @author Glom
 * @date 2022/7/9 7:45 Copyright 2022 user. All rights reserved.
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

    fun toSponge(): EventOrder = when (level) {
        1 -> EventOrder.PRE
        2 -> EventOrder.AFTER_PRE
        3 -> EventOrder.FIRST
        4 -> EventOrder.EARLY
        5 -> EventOrder.DEFAULT
        6 -> EventOrder.LATE
        7 -> EventOrder.LAST
        8 -> EventOrder.BEFORE_POST
        9 -> EventOrder.POST
        else -> EventOrder.DEFAULT
    }
}