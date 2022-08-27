package com.skillw.pouvoir.internal.core.function.functions.common.bukkit

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import org.bukkit.Sound
import taboolib.common5.Coerce
import taboolib.library.xseries.XSound

/**
 * @className FunctionBlock
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionSound : PouFunction<Sound>(
    "sound"
) {
    override fun execute(parser: Parser): Sound {
        with(parser) {
            val token = parseString()
            return XSound.matchXSound(token)
                .run { if (this.isPresent) get().parseSound() else Coerce.toEnum(token, Sound::class.java) }
                ?: error("Sound not found: $token")
        }
    }
}