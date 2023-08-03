package com.skillw.pouvoir.api.message

import com.skillw.pouvoir.api.PouvoirAPI.analysis
import com.skillw.pouvoir.api.map.DataMap
import com.skillw.pouvoir.internal.core.function.context.SimpleContext
import org.bukkit.entity.Player
import taboolib.common.util.unsafeLazy

/**
 * @className MessageData
 *
 * @param player Player 用来作为解析参数的玩家
 * @param map HashMap 参数
 * @author Glom
 * @date 2022/12/15 17:00 Copyright 2022 user. All rights reserved.
 */
class MessageData(val player: Player? = null, map: HashMap<String, Any> = HashMap()) : DataMap() {

    private val context by unsafeLazy {
        player?.let { SimpleContext(hashMapOf("player" to player, "entity" to player)) } ?: SimpleContext()
    }

    override fun get(key: String): Any? {
        return this[key]?.run { if (this is String) analysis(context) else this }
    }

    init {
        putAll(map)
    }
}