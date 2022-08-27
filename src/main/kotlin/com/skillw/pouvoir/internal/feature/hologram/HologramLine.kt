package com.skillw.pouvoir.internal.feature.hologram

import org.bukkit.Location
import org.bukkit.entity.Player

internal class HologramLine(location: Location, line: String, vararg viewers: Player) {
    private val holoLine = PouHolo.create(location, line, *viewers)

    init {
        for (viewer in viewers) {
            holoLine.visible(viewer, true)
        }
    }

    constructor(location: Location, line: String, viewers: Set<Player>) : this(location, line) {
        for (viewer in viewers) {
            holoLine.visible(viewer, true)
        }
    }

    fun update(line: String) {
        if (!holoLine.isDeleted) {
            holoLine.update(line)
        }
    }

    fun teleport(location: Location) {
        if (!holoLine.isDeleted) {
            holoLine.teleport(location)
        }
    }

    fun delete() {
        holoLine.delete()
    }

    fun destroy() {
        holoLine.destroy()
    }
}