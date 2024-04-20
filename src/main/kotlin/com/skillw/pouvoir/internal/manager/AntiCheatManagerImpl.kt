package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.AntiCheatManager
import org.bukkit.entity.Player

internal object AntiCheatManagerImpl : AntiCheatManager() {
    override val key = "AntiCheatManager"
    override val priority = 5
    override val subPouvoir = Pouvoir
    override fun bypass(player: Player) {
        values.forEach { it.bypass(player) }
    }

    override fun recover(player: Player) {
        values.forEach { it.recover(player) }
    }
}