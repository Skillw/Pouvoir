package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.feature.hologram.IHologram
import com.skillw.pouvoir.api.manager.sub.HologramManager
import com.skillw.pouvoir.internal.feature.hologram.Hologram
import org.bukkit.Location
import org.bukkit.entity.Player

internal object HologramManagerImpl : HologramManager() {
    override val key = "HologramManagerImpl"
    override val priority = 3
    override val subPouvoir = Pouvoir

    override fun createHologram(location: Location, content: List<String>, players: Set<Player>): IHologram {
        return Hologram(location, content, players)
    }
}