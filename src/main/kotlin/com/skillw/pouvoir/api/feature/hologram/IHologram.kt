package com.skillw.pouvoir.api.feature.hologram

import com.skillw.pouvoir.api.plugin.map.component.Keyable
import org.bukkit.Location
import org.bukkit.entity.Player

/** 全息接口 */
interface IHologram : Keyable<String> {

    /** 坐标 */
    var location: Location

    /** 内容 */
    val content: List<String>

    /** 可看到的玩家 */
    val viewers: MutableSet<Player>

    /**
     * 传送
     *
     * @param location 坐标
     */
    fun teleport(location: Location)

    /**
     * 更新内容
     *
     * @param content 新内容
     */
    fun update(content: List<String>)

    /** 删除 */
    fun delete()

    /** 销毁 */
    fun destroy()
}