package com.skillw.pouvoir.api.message

import org.bukkit.entity.Player

/**
 * @className Messager
 *
 * @author Glom
 * @date 2022/12/15 16:57 Copyright 2022 user. All rights reserved.
 */
fun interface Messager {
    fun sendTo(vararg players: Player)
}
