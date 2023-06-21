package com.skillw.pouvoir.internal.feature.hologram

import com.skillw.pouvoir.api.feature.hologram.IHologram
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.concurrent.ConcurrentHashMap

class Hologram private constructor(var location: Location, val content: List<String>) : IHologram {

    override val key = (location.toString().hashCode() + content.hashCode()).toString()
    val viewers = ConcurrentHashSet<Player>()


    private val lines = ConcurrentHashMap<Int, HologramLine>()

    init {
        content.forEachIndexed { index, line ->
            lines[index] =
                HologramLine(location.clone().add(0.0, (((content.size - 1) - index) * 0.3), 0.0), line, this.viewers)
        }
    }

    constructor(location: Location, content: List<String>, vararg viewers: Player) : this(location, content) {
        this.viewers.addAll(viewers)
    }

    constructor(location: Location, content: List<String>, viewers: Set<Player>) : this(location, content) {
        this.viewers.addAll(viewers)
    }

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