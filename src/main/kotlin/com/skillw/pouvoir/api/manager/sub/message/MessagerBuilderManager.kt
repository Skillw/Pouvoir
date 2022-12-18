package com.skillw.pouvoir.api.manager.sub.message

import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.LowerKeyMap
import com.skillw.pouvoir.api.message.MessageData
import com.skillw.pouvoir.api.message.Messager
import com.skillw.pouvoir.api.message.MessagerBuilder
import org.bukkit.entity.Player


/**
 * MessageType type manager
 *
 * 用于注册自定义消息类型
 *
 * 编写Message的实现类，并注册它的Builder以自定义消息类型
 *
 * @constructor Create empty MessageType type manager
 */
abstract class MessagerBuilderManager : Manager, LowerKeyMap<MessagerBuilder>() {
    /**
     * 构建Messager
     *
     * @param key 类型
     * @param data 数据
     * @return Messager
     */
    abstract fun build(key: String, data: MessageData): Messager?

    /**
     * 构建Messager
     *
     * @param key 类型
     * @param player 玩家
     * @param receiver 接收器
     * @return Messager
     * @receiver 处理MessagerData
     */
    abstract fun build(key: String, player: Player, receiver: MessageData.() -> Unit): Messager?
}
