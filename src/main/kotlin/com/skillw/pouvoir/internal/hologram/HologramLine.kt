package com.skillw.pouvoir.internal.hologram

import org.bukkit.Location
import org.bukkit.entity.Player

internal class HologramLine(location: Location, line: String, vararg viewers: Player) {
    private val armorStand = PouHolo.create(location, line, *viewers)

    init {
        for (viewer in viewers) {
            armorStand.visible(viewer, true)
        }
    }

    constructor(location: Location, line: String, viewers: Set<Player>) : this(location, line) {
        for (viewer in viewers) {
            armorStand.visible(viewer, true)
        }
    }

    fun update(line: String) {
        if (!armorStand.isDeleted) {
            armorStand.update(line)
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