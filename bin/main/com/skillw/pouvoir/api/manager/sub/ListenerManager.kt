package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.map.LowerMap
import taboolib.common.platform.event.ProxyListener

/**
 * Listener manager Bukkit监听器管理器
 *
 * 维护热注册注销的监听器
 *
 * @constructor Create empty Listener manager
 */
abstract class ListenerManager : LowerMap<ProxyListener>(), Manager