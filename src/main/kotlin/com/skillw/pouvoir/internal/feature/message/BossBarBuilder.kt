package com.skillw.pouvoir.internal.feature.message

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.message.MessageData
import com.skillw.pouvoir.api.message.Messager
import com.skillw.pouvoir.api.message.MessagerBuilder
import com.skillw.pouvoir.util.PlayerUtils
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle


@AutoRegister
internal object BossBarBuilder : MessagerBuilder("boss_bar") {
    /**
     * 参数:
     * - text 主标题
     * - color BarColor
     * - style BarStyle
     * - progress 进度(0到1)
     *
     * @param data MessageData
     * @return Messager
     */
    override fun build(data: MessageData): Messager {
        val text = data.get("text", "")
        val color = data.get("color", BarColor.PURPLE)
        val style = data.get("style", BarStyle.SEGMENTED_10)
        val progress = data.get("progress", 1.0)
        return Messager { players ->
            players.forEach {
                PlayerUtils.sendBossBar(it, text, color, style, progress)
            }
        }
    }
}