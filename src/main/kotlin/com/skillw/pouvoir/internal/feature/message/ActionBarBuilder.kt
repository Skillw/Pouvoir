package com.skillw.pouvoir.internal.feature.message

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.message.MessageData
import com.skillw.pouvoir.api.message.Messager
import com.skillw.pouvoir.api.message.MessagerBuilder
import com.skillw.pouvoir.util.PlayerUtils


@AutoRegister
internal object ActionBarBuilder : MessagerBuilder("action_bar") {
    /**
     * 参数:
     * - text 文本内容
     * - stay 持续时间(tick)
     *
     * @param data MessageData
     * @return Messager
     */
    override fun build(data: MessageData): Messager {
        val text = data.get("text", "")
        val stay = data.get("stay", 20L)
        return Messager { players -> players.forEach { PlayerUtils.sendActionBar(it, text, stay) } }
    }
}