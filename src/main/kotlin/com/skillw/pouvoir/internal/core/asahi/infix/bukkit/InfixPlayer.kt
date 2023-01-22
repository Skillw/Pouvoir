package com.skillw.pouvoir.internal.core.asahi.infix.bukkit

import com.skillw.asahi.api.annotation.AsahiInfix
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix
import com.skillw.pouvoir.util.sendActionBar
import com.skillw.pouvoir.util.sendTitle
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit
import taboolib.module.chat.colored
import taboolib.platform.util.bukkitPlugin
import taboolib.platform.util.getMetaFirst
import taboolib.platform.util.setMeta

/**
 * @className ActionPlayer
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AsahiInfix
object InfixPlayer : BaseInfix<Player>(Player::class.java, "bukkit") {
    init {

        infix("actionbar") { player ->
            sendActionBar(player, parse<String>().colored(), parse(), bukkitPlugin)
        }

        infix("title") { player ->
            sendTitle(
                player,
                parse<String>().colored().run { if (this == "null") null else this },
                parse<String>().colored().run { if (this == "null") null else this },
                parse(),
                parse(),
                parse()
            )
        }

        infix("message") { player ->
            player.sendMessage(parse<String>().colored())
        }

        infix("messages") { player ->
            player.sendMessage(parse<List<Any>>().map { it.toString().colored() }.toTypedArray())
        }

        infix("command") { player ->
            submit {
                player.performCommand(parse())
            }
        }

        infix("allowFlight") { player ->
            if (expect("to"))
                player.allowFlight = parse()
            else
                player.allowFlight
        }

        infix("bedSpawn") { player ->
            if (expect("to"))
                player.bedSpawnLocation = parse()
            else
                player.bedSpawnLocation
        }

        infix("blocking") { player ->
            player.isBlocking
        }

        infix("cooldown") { player ->
            val material = parse<Material>()
            if (expect("to"))
                player.setCooldown(material, parse())
            else
                player.getCooldown(material)
        }

        infix("name") { player ->
            player.name
        }

        infix("healthScale") { player ->
            if (expect("to"))
                submit { player.healthScale = parse() }
            else
                player.healthScale
        }

        infix("isSneaking") { player ->
            player.isSneaking
        }

        infix("isSleeping") { player ->
            player.isSleeping
        }

        infix("exp") { player ->
            if (expect("to"))
                player.exp = parse()
            else
                player.exp
        }

        infix("level") { player ->
            if (expect("to"))
                player.level = parse()
            else
                player.level
        }

        infix("playSound") { player ->
            val sound = parse<Sound>()
            var volume = 1f
            var pitch = 1f
            if (expect("with")) {
                volume = parse()
                pitch = parse()
            }
            player.playSound(player.location, sound, volume, pitch)
        }

        infix("stopSound") { player ->
            val sound = parse<Sound>()
            player.stopSound(sound)
        }

        infix("metadata") { player ->
            val key = parse<String>()
            if (expect("to"))
                player.setMeta(key, parse())
            else
                player.getMetaFirst(key)
        }
    }
}