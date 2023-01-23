package com.skillw.pouvoir.internal.core.asahi.util

import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.function.adaptCommandSender

/**
 * @className PlayerAsOp
 *
 * @author Glom
 * @date 2023/1/23 17:58 Copyright 2023 user. All rights reserved.
 */
class PlayerAsOp(val player: Player) : ProxyCommandSender by adaptCommandSender(player) {
    override var isOp: Boolean
        get() = true
        set(value) {}
}