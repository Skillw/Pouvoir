package com.skillw.pouvoir.internal.feature.message

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.message.MessageData
import com.skillw.pouvoir.api.message.Messager
import com.skillw.pouvoir.api.message.MessagerBuilder
import com.skillw.pouvoir.util.PlayerUtils
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import taboolib.common5.Coerce


@AutoRegister
internal object BossBarBuilder : MessagerBuilder("boss_bar") {
    /**
     * 参数:
     * - text 主标题
     * - color BarColor的id 字符串
     * - style BarStyle的id 字符串
     * - progress 进度(0到1)
     *
     * @param data MessageData
     * @return Messager
     */
    override fun build(data: MessageData): Messager {
        val text = data.get("text", "")
        val color = Coerce.toEnum(data.get("color", "PURPLE"), BarColor::class.java)
        val style = Coerce.toEnum(data.get("style", "SEGMENTED_10"), BarStyle::class.java)
        val progress = data.get("progress", 1.0)
        return Messager { players ->
            players.forEach {
                PlayerUtils.sendBossBar(it, text, color, style, progress)
            }
        }
    }
}