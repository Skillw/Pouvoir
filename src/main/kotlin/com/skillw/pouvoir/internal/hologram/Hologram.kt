package com.skillw.pouvoir.internal.hologram

import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.concurrent.ConcurrentHashMap

class Hologram(var location: Location, val content: List<String>) {
    val viewers = ConcurrentHashSet<Player>()

    constructor(location: Location, content: List<String>, vararg viewers: Player) : this(location, content) {
        this.viewers.addAll(viewers)
        content.forEachIndexed { index, line ->
            map[index] =
                HologramLine(location.clone().add(0.0, (((content.size - 1) - index) * 0.3), 0.0), line, this.viewers)
        }
    }

    constructor(location: Location, content: List<String>, viewers: Set<Player>) : this(location, content) {
        this.viewers.addAll(viewers)
        content.forEachIndexed { index, line ->
            map[index] =
                HologramLine(location.clone().add(0.0, (((content.size - 1) - index) * 0.3), 0.0), line, this.viewers)
        }
    }

    private val map = ConcurrentHashMap<Int, HologramLine>()

    fun teleport(location: Location) {
        this.location = location
        map.forEach { (index, singleLine) ->
            singleLine.teleport(location.clone().add(0.0, (((map.size - 1) - index) * 0.3), 0.0))
        }
    }

    fun update(content: List<String>) {
        delete()
        map.clear()
        content.forEachIndexed { index, line ->
            map[index] = HologramLine(
                location.clone().add(0.0, (((content.size - 1) - index) * 0.3), 0.0),
                line,
                viewers
            )
        }
    }

    fun delete() {
        map.forEach { it.value.delete() }
    }

    fun destroy() {
        map.forEach { it.value.destroy() }
    }


}