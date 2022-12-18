package com.skillw.pouvoir.internal.feature.message

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.hologram.HologramBuilder
import com.skillw.pouvoir.api.message.MessageData
import com.skillw.pouvoir.api.message.Messager
import com.skillw.pouvoir.api.message.MessagerBuilder
import org.bukkit.Bukkit
import org.bukkit.Location


@AutoRegister
internal object HoloGroupBuilder : MessagerBuilder("holo") {
    /**
     * 参数:
     * - content 内容
     * - location 坐标
     * - stay 持续时间(tick)
     *
     * 下面这两项是全息运动，选填
     * - time 运动次数
     * - finalLocation 终点
     *
     * @param data MessageData
     * @return Messager
     */
    override fun build(data: MessageData): Messager {
        val player = data.player
        with(data) {
            val location = getOrDefault("location") {
                player?.eyeLocation?.add(0.0, 1.0, 0.0) ?: Location(
                    Bukkit.getWorlds().first(),
                    0.0,
                    0.0,
                    0.0
                )
            }
            val content = get("content", emptyList<String>())
            val stay = get("stay", 20L)
            val time = get("time", -1)
            val finalLocation = getOrDefault("finalLocation") { location }
            val builder = HologramBuilder(location).content(content).stay(stay).animation(time, finalLocation)
            return Messager { builder.viewers(*it).build() }
        }
    }
}