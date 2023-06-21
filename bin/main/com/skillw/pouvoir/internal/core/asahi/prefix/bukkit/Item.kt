package com.skillw.pouvoir.internal.core.asahi.prefix.bukkit

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import org.bukkit.Material
import taboolib.library.xseries.XMaterial

/**
 * @className Item
 *
 * @author Glom
 * @date 2023/1/14 0:33 Copyright 2023 user. All rights reserved.
 */

@AsahiPrefix
private fun material() = prefixParser<Material> {
    val token = quest<String>()
    result {
        val id = token.get()
        XMaterial.matchXMaterial(id)
            .run { if (this.isPresent) get().parseMaterial() else Material.matchMaterial(id) }
            ?: error("Material not found: $token")
    }
}