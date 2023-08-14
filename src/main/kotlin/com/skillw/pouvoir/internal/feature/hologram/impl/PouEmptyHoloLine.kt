package com.skillw.pouvoir.internal.feature.hologram.impl

import com.skillw.pouvoir.internal.feature.hologram.PouHolo
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * @className PouEmptyHoloLine
 *
 * @author Glom
 * @date 2023/1/17 13:10 Copyright 2023 user. All rights reserved.
 */
class PouEmptyHoloLine : PouHolo {
    override val isDeleted: Boolean
        get() = true

    override fun destroy() {

    }

    override fun respawn() {

    }

    override fun spawn() {
        
    }

    override fun teleport(location: Location) {

    }

    override fun delete() {

    }

    override fun visible(viewer: Player, visible: Boolean) {

    }

    override fun update(line: String) {

    }
}