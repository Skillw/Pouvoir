package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.containerManager
import com.skillw.pouvoir.api.manager.sub.message.PersonalManager
import com.skillw.pouvoir.internal.manager.PouConfig.defaultMessage
import com.skillw.pouvoir.internal.manager.PouConfig.lockedDefaultMessage
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.SubscribeEvent


/**
 * Inline function action manager
 *
 * @constructor Create empty Inline function manager
 */
object PersonalManagerImpl : PersonalManager() {
    override val key = "PersonalManager"
    override val priority = 4
    override val subPouvoir = Pouvoir
    override fun get(player: Player): String {
        return if (lockedDefaultMessage) defaultMessage
        else get(player.uniqueId) ?: defaultMessage
    }

    @SubscribeEvent
    fun join(event: PlayerJoinEvent) {
        val player = event.player
        val uuid = player.uniqueId
        val message = containerManager[uuid.toString(), "personal_message"] ?: defaultMessage
        this[uuid] = message
    }

    @SubscribeEvent
    fun exit(event: PlayerQuitEvent) {
        val player = event.player
        val uuid = player.uniqueId
        if (!lockedDefaultMessage)
            containerManager[uuid.toString(), "personal_message"] = player.getMessageType()
    }

    override fun onDisable() {
        if (lockedDefaultMessage) return
        entries.forEach { (uuid, message) ->
            containerManager[uuid.toString(), "personal_message"] = message
        }
    }

}