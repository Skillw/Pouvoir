package com.skillw.pouvoir.internal.feature.hologram

import com.skillw.pouvoir.api.feature.hologram.IHologram
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.concurrent.ConcurrentHashMap

class Hologram(override var location: Location, override val content: List<String>) : IHologram {

    override val key = (location.toString().hashCode() + content.hashCode()).toString()
    override val viewers = ConcurrentHashSet<Player>()

    constructor(location: Location, content: List<String>, vararg viewers: Player) : this(location, content) {
        this.viewers.addAll(viewers)
        content.forEachIndexed { index, line ->
            lines[index] =
                HologramLine(location.clone().add(0.0, (((content.size - 1) - index) * 0.3), 0.0), line, this.viewers)
        }
    }

    constructor(location: Location, content: List<String>, viewers: Set<Player>) : this(location, content) {
        this.viewers.addAll(viewers)
        content.forEachIndexed { index, line ->
            lines[index] =
                HologramLine(location.clone().add(0.0, (((content.size - 1) - index) * 0.3), 0.0), line, this.viewers)
        }
    }

    private val lines = ConcurrentHashMap<Int, HologramLine>()

    override fun teleport(location: Location) {
        this.location = location
        lines.forEach { (index, singleLine) ->
            singleLine.teleport(location.clone().add(0.0, (((lines.size - 1) - index) * 0.3), 0.0))
        }
    }

    override fun update(content: List<String>) {
        delete()
        lines.clear()
        content.forEachIndexed { index, line ->
            lines[index] = HologramLine(
                location.clone().add(0.0, (((content.size - 1) - index) * 0.3), 0.0),
                line,
                viewers
            )
        }
    }

    override fun delete() {
        lines.forEach { it.value.delete() }
    }

    override fun destroy() {
        lines.forEach { it.value.destroy() }
    }


}