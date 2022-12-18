package com.skillw.pouvoir.internal.feature.message

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.message.MessageData
import com.skillw.pouvoir.api.message.Messager
import com.skillw.pouvoir.api.message.MessagerBuilder


@AutoRegister
internal object ChatBuilder : MessagerBuilder("chat") {
    /**
     * 参数:
     * - text 文本内容
     *
     * @param data MessageData
     * @return Messager
     */
    override fun build(data: MessageData): Messager {
        val text = data.get("text", "")
        return Messager { players -> players.forEach { it.sendMessage(text) } }
    }
}