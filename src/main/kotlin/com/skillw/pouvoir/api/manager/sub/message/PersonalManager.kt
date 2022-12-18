package com.skillw.pouvoir.api.manager.sub.message

import com.skillw.pouvoir.Pouvoir.personalManager
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.BaseMap
import org.bukkit.entity.Player
import java.util.*


/**
 * Personal Manager
 *
 * 用于管理每个玩家的消息类型设置
 *
 * @constructor Create empty MessageType type manager
 */
abstract class PersonalManager : Manager, BaseMap<UUID, String>() {
    abstract fun get(player: Player): String

    companion object {
        fun Player.getMessageType(): String {
            return personalManager.get(this)
        }
    }
}
