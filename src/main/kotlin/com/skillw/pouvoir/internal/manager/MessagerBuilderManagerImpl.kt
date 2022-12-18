package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.message.MessagerBuilderManager
import com.skillw.pouvoir.api.message.MessageData
import com.skillw.pouvoir.api.message.Messager
import org.bukkit.entity.Player


/**
 * Inline function action manager
 *
 * @constructor Create empty Inline function manager
 */
object MessagerBuilderManagerImpl : MessagerBuilderManager() {
    override val key = "MessagerBuilderManager"
    override val priority = 4
    override val subPouvoir = Pouvoir
    override fun build(key: String, data: MessageData): Messager? {
        return get(key)?.build(data)
    }

    override fun build(key: String, player: Player, receiver: MessageData.() -> Unit): Messager? {
        return build(key, MessageData(player).apply(receiver))
    }

}