package com.skillw.pouvoir.api.feature.anticheat

import com.skillw.pouvoir.Pouvoir.antiCheatManager
import com.skillw.pouvoir.api.plugin.map.component.Registrable
import org.bukkit.entity.Player

/**
 * @className BypassCheat
 *
 * @author Glom
 * @date 2023/8/6 20:54 Copyright 2024 Glom. 
 */
interface BypassCheat : Registrable<String> {
    fun bypass(player: Player)
    fun recover(player: Player) {}

    override fun register() {
        antiCheatManager.register(this)
    }
}