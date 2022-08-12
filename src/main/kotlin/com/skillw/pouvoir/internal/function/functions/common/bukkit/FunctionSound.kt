package com.skillw.pouvoir.internal.function.functions.common.bukkit

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser
import com.skillw.pouvoir.api.function.parse.Parser.Companion.parse
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import taboolib.common5.Coerce

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
    override fun execute(parser: Parser): Sound? {
        with(parser) {
            val sound = Coerce.toEnum(parseAny(), Sound::class.java) ?: return null
            val location = if (except("[")) {
                val world = Bukkit.getWorld(parseString()) ?: return null
                except(",")
                val x = parseDouble()
                except(",")
                val y = parseDouble()
                except(",")
                val z = parseDouble()
                if (except("]")) Location(world, x, y, z) else return null
            } else parse<Location>() ?: return null
            var volume = 1f
            var pitch = 1f
            if (except("with")) {
                volume = parseFloat()
                pitch = parseFloat()
            }
            location.world?.playSound(location, sound, volume, pitch)
            return sound
        }
    }
}