package com.skillw.pouvoir.internal.hook

import com.skillw.pouvoir.Pouvoir
import org.bukkit.entity.Player
import taboolib.platform.compat.PlaceholderExpansion

object PlaceholderAPIHooker : PlaceholderExpansion {
    override val identifier = "pou"
    override fun onPlaceholderRequest(player: Player?, args: String): String {
        player ?: return args
        return Pouvoir.pouPlaceHolderAPI.replace(player, "%pou_$args%")
    }
}
