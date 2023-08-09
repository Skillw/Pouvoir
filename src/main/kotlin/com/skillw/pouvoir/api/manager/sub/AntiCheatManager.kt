package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.feature.anticheat.BypassCheat
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.map.KeyMap
import org.bukkit.entity.Player

/**
 * 反作弊管理器
 *
 * @constructor Create empty Listener manager
 */
abstract class AntiCheatManager : KeyMap<String, BypassCheat>(), Manager {
    abstract fun bypass(player: Player)
    abstract fun recover(player: Player)
}