package com.skillw.pouvoir.internal.feature.hologram.impl

import com.skillw.pouvoir.internal.feature.hologram.PouHolo
import ink.ptms.adyeshach.api.AdyeshachAPI
import ink.ptms.adyeshach.common.entity.EntityTypes
import ink.ptms.adyeshach.common.entity.type.AdyArmorStand
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import taboolib.module.chat.colored

/**
 * @className PouHolographicDisplaysLine
 *
 * @author Glom
 * @date 2022/7/31 19:02 Copyright 2022 user. All rights reserved.
 */
internal class PouAdyHologramsLine(location: Location, line: String, vararg viewers: Player) : PouHolo {
    companion object {
        @JvmStatic
        val enable by lazy {
            Bukkit.getPluginManager().isPluginEnabled("Adyeshach")
        }
    }

    init {
        viewers.forEach {
            visible(it, true)
        }
    }

    private val stand =
        AdyeshachAPI.getEntityManagerPublicTemporary().create(EntityTypes.ARMOR_STAND, location) {
            val npc = it as AdyArmorStand
            npc.setSmall(true)
            npc.setMarker(true)
            npc.setBasePlate(false)
            npc.setInvisible(true)
            npc.setCustomName(line)
            npc.setCustomNameVisible(line.isNotEmpty())
            viewers.forEach { player ->
                npc.visible(player, true)
            }
        } as AdyArmorStand


    override val isDeleted: Boolean
        get() = stand.isDeleted

    override fun destroy() {
        stand.destroy()
    }

    override fun respawn() {
        stand.respawn()
    }

    override fun spawn() {
        stand.respawn()
    }

    override fun teleport(location: Location) {
        stand.teleport(location)
    }

    override fun delete() {
        stand.delete()
        stand.destroy()
    }

    override fun visible(viewer: Player, visible: Boolean) {
        stand.visible(viewer, visible)
    }

    override fun update(line: String) {
        if (!stand.isDeleted) {
            stand.destroy()
            stand.setCustomName(line.colored())
            stand.setCustomNameVisible(line.isNotEmpty())
            stand.respawn()
        }
    }
}