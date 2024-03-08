package com.skillw.pouvoir.internal.feature.hologram

import com.skillw.pouvoir.internal.feature.hologram.impl.PouAdyHologramsLine
import com.skillw.pouvoir.internal.feature.hologram.impl.PouDecentHologramsLine
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * @className PouHolo
 *
 * @author Glom
 * @date 2022/7/31 18:59 Copyright 2022 user.
 */
interface PouHolo {
    val isDeleted: Boolean
    fun destroy()
    fun respawn()
    fun spawn()
    fun teleport(location: Location)
    fun delete()
    fun visible(viewer: Player, visible: Boolean)
    fun update(line: String)

    companion object {
        @JvmStatic
        fun create(location: Location, line: String, vararg viewers: Player): PouHolo? {
            return when {
                PouDecentHologramsLine.enable -> {
                    PouDecentHologramsLine(location, line, *viewers)
                }

                PouAdyHologramsLine.enable -> {
                    PouAdyHologramsLine(location, line, *viewers)
                }

                else -> {
                    taboolib.common.platform.function.warning("PouHologram required Adyeshach or DecentHologram")
                    null
                }
            }
        }
    }
}