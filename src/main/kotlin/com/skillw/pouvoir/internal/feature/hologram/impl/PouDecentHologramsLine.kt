package com.skillw.pouvoir.internal.feature.hologram.impl

import com.skillw.pouvoir.internal.feature.hologram.PouHolo
import eu.decentsoftware.holograms.api.DHAPI
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

/**
 * @className PouHolographicDisplaysLine
 *
 * @author Glom
 * @date 2022/7/31 19:02 Copyright 2022 user. All rights reserved.
 */
internal class PouDecentHologramsLine(location: Location, line: String, vararg viewers: Player) : PouHolo {

    init {
        viewers.forEach {
            visible(it, true)
        }
    }

    companion object {
        @JvmStatic
        val enable by lazy {
            Bukkit.getPluginManager().isPluginEnabled("DecentHolograms")
        }
    }

    private val hologram = DHAPI.createHologram(UUID.randomUUID().toString(), location, false, listOf(line))
    override val isDeleted: Boolean
        get() = !hologram.isEnabled

    override fun destroy() {
        hologram.destroy()
    }

    override fun respawn() {
        hologram.updateAll()
    }

    override fun spawn(location: Location) {
        DHAPI.moveHologram(hologram, location)
    }

    override fun teleport(location: Location) {
        DHAPI.moveHologram(hologram, location)
    }

    override fun delete() {
        hologram.hideAll()
        hologram.disable()
        hologram.delete()
        hologram.destroy()
    }

    override fun visible(viewer: Player, visible: Boolean) {
        if (visible)
            hologram.show(viewer, 0)
        else
            hologram.hide(viewer)
    }

    override fun update(line: String) {
        DHAPI.setHologramLine(hologram, 0, line)
    }
}