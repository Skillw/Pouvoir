package com.skillw.pouvoir.internal.core.asahi.prefix.bukkit

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import org.bukkit.Sound
import taboolib.common5.Coerce
import taboolib.library.xseries.XSound

/**
 * @className Player
 *
 * @author Glom
 * @date 2023/1/14 0:32 Copyright 2023 user. All rights reserved.
 */

@AsahiPrefix
private fun sound() = prefixParser<Sound> {
    val sound = quest<String>()
    result {
        val id = sound.get()
        XSound.matchXSound(id)
            .run { if (this.isPresent) get().parseSound() else Coerce.toEnum(id, Sound::class.java) }
            ?: error("Sound not found: $id")
    }
}