package com.skillw.pouvoir.internal.feature.message

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.message.MessageData
import com.skillw.pouvoir.api.message.Messager
import com.skillw.pouvoir.api.message.MessagerBuilder
import com.skillw.pouvoir.util.PlayerUtils


@AutoRegister
internal object TitleBuilder : MessagerBuilder("title") {
    /**
     * 参数:
     * - title 主标题
     * - subTitle 副标题
     * - fadeIn 渐入时间(tick)
     * - stay 滞留时间(tick)
     * - fadeOut 渐出时间(tick)
     *
     * @param data MessageData
     * @return Messager
     */
    override fun build(data: MessageData): Messager {
        val title = data.get("title", "")
        val subTitle = data.get("subTitle", "")
        val fadeIn = data.get("fadeIn", 0)
        val stay = data.get("stay", 20)
        val fadeOut = data.get("fadeOut", 0)
        return Messager { players ->
            players.forEach {
                PlayerUtils.sendTitle(
                    it,
                    title,
                    subTitle,
                    fadeIn,
                    stay,
                    fadeOut
                )
            }
        }
    }
}