package com.skillw.pouvoir.internal.core.function.action

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.PouAction
import com.skillw.pouvoir.util.PlayerUtils
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Entity
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
@AutoRegister
object ActionPlayer : PouAction<Player>(Player::class.java) {
    init {
        addExec("location") { player ->
            player.location
        }

        addExec("teleport") { player ->
            except("to")
            when (val any = parseAny()) {
                is Entity -> player.teleport(any)
                is Location -> player.teleport(any)
                else -> error("Wrong argument type, only Location / Entity")
            }
        }

        addExec("actionbar") { player ->
            PlayerUtils.sendActionBar(player, parseString().colored(), parseLong(), bukkitPlugin)
        }

        addExec("title") { player ->
            PlayerUtils.sendTitle(
                player,
                parseString().colored().run { if (this == "null") null else this },
                parseString().colored().run { if (this == "null") null else this },
                parseInt(),
                parseInt(),
                parseInt()
            )
        }

        addExec("message") { player ->
            player.sendMessage(parseString().colored())
        }

        addExec("messages") { player ->
            player.sendMessage(*parseList().map { it.toString().colored() }.toTypedArray())
        }

        addExec("command") { player ->
            submit {
                player.performCommand(parseString())
            }
        }

        addExec("allowFlight") { player ->
            if (except("to"))
                player.allowFlight = parseBoolean()
            else
                player.allowFlight
        }

        addExec("bedSpawn") { player ->
            if (except("to"))
                player.bedSpawnLocation = parse<Location>()
            else
                player.bedSpawnLocation
        }

        addExec("blocking") { player ->
            player.isBlocking
        }

        addExec("cooldown") { player ->
            val material = parse<Material>()
            if (except("to"))
                player.setCooldown(material, parse())
            else
                player.getCooldown(material)
        }

        addExec("name") { player ->
            player.name
        }

        addExec("displayName") { player ->
            player.displayName
        }

        addExec("health") { player ->
            if (except("to"))
                player.health = parseDouble()
            else
                player.health
        }

        addExec("maxHealth") { player ->
            if (except("to"))
                player.maxHealth = parseDouble()
            else
                player.maxHealth
        }

        addExec("healthScale") { player ->
            if (except("to"))
                submit { player.healthScale = parseDouble() }
            else
                player.healthScale
        }

        addExec("isSneaking") { player ->
            player.isSneaking
        }

        addExec("isSleeping") { player ->
            player.isSleeping
        }

        addExec("exp") { player ->
            if (except("to"))
                player.exp = parseFloat()
            else
                player.exp
        }

        addExec("level") { player ->
            if (except("to"))
                player.level = parseInt()
            else
                player.level
        }

        addExec("playSound") { player ->
            val sound = parse<Sound>()
            var volume = 1f
            var pitch = 1f
            if (except("with")) {
                volume = parseFloat()
                pitch = parseFloat()
            }
            player.playSound(player.location, sound, volume, pitch)
        }

        addExec("stopSound") { player ->
            val sound = parse<Sound>()
            player.stopSound(sound)
        }

        addExec("velocity") { player ->
            if (except("to"))
                player.velocity = parse()
            else
                player.velocity
        }

        addExec("metadata") { player ->
            val key = parseString()
            if (except("to"))
                player.setMeta(key, parseAny())
            else
                player.getMetaFirst(key)
        }
    }
}