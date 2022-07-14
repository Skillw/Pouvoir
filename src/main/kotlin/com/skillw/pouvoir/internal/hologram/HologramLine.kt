package com.skillw.pouvoir.internal.hologram

import org.bukkit.Location
import org.bukkit.entity.Player
import taboolib.module.chat.colored
import java.util.*

private var index = 114514

private fun nextInt(): Int {
    return index++
}

class HologramLine(location: Location, line: String) {
    val viewers = Collections.synchronizedSet(HashSet<Player>())

    constructor(location: Location, line: String, vararg viewers: Player) : this(location, line) {
        this.viewers.addAll(viewers)
        for (viewer in viewers) {
            armorStand.visible(viewer, true)
        }
    }

    constructor(location: Location, line: String, viewers: Set<Player>) : this(location, line) {
        this.viewers.addAll(viewers)
        for (viewer in viewers) {
            armorStand.visible(viewer, true)
        }
    }

    private val armorStand =
        PouArmorStand(nextInt(), location) {
            it.setSmall(true)
            it.setMarker(true)
            it.setBasePlate(false)
            it.setInvisible(true)
            it.setCustomName(line.colored())
            it.setCustomNameVisible(true)
        }


    fun update(line: String) {
        if (!armorStand.isDeleted) {
            armorStand.destroy()
            armorStand.setCustomName(line.colored())
            armorStand.respawn()
        }
    }

    fun teleport(location: Location) {
        if (!armorStand.isDeleted) {
            armorStand.teleport(location)
        }
    }

    fun delete() {
        armorStand.delete()
    }

    fun destroy() {
        armorStand.destroy()
    }
}