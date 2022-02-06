package com.skillw.pouvoir.internal.listener

import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.expansion.releaseDataContainer
import taboolib.expansion.setupDataContainer

object PlayerListener {
    @SubscribeEvent
    fun onPlayerJoinEvent(e: PlayerJoinEvent) {
        
        e.player.setupDataContainer()
    }

    @SubscribeEvent
    fun onPlayerQuitEvent(e: PlayerQuitEvent) {
        e.player.releaseDataContainer()
    }
}