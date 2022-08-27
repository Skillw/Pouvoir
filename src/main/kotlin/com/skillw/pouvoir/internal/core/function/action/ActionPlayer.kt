package com.skillw.pouvoir.internal.core.function.action

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.IAction
import com.skillw.pouvoir.api.function.parser.Parser
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
object ActionPlayer : IAction {
    override val actions: Set<String>
        get() = hashSetOf(
            "location",
            "teleport",
            "actionbar",
            "title",
            "message",
            "messages",
            "command",
            "allowFlight",
            "bedSpawn",
            "blocking",
            "cooldown",
            "name",
            "displayName",
            "health",
            "maxHealth",
            "healthScale",
            "isSneaking",
            "isSleeping",
            "exp",
            "level",
            "playSound",
            "stopSound",
            "velocity",
            "metadata",
        )
    override val type: Class<*>
        get() = Player::class.java

    override fun action(parser: Parser, obj: Any, action: String): Any? {
        if (obj !is Player) error("obj is not a Player")
        with(parser) {
            return when (action) {
                "location" -> {
                    return obj.location
                }

                "teleport" -> {
                    except("to")
                    when (val any = parseAny()) {
                        is Entity -> obj.teleport(any)
                        is Location -> obj.teleport(any)
                        else -> error("Wrong argument type, only Location / Entity")
                    }
                }

                "actionbar" -> {
                    PlayerUtils.sendActionBar(obj, parseString().colored(), parseLong(), bukkitPlugin)
                }

                "title" -> {
                    PlayerUtils.sendTitle(
                        obj,
                        parseString().colored().run { if (this == "null") null else this },
                        parseString().colored().run { if (this == "null") null else this },
                        parseInt(),
                        parseInt(),
                        parseInt()
                    )
                }

                "message" -> {
                    obj.sendMessage(parseString().colored())
                }

                "messages" -> {
                    obj.sendMessage(*parseList().map { it.toString().colored() }.toTypedArray())
                }

                "command" -> {
                    submit {
                        obj.performCommand(parseString())
                    }
                }

                "allowFlight" -> {
                    if (except("to"))
                        obj.allowFlight = parseBoolean()
                    else
                        return obj.allowFlight
                }

                "bedSpawn" -> {
                    if (except("to"))
                        obj.bedSpawnLocation = parse<Location>()
                    else
                        return obj.bedSpawnLocation
                }

                "blocking" -> {
                    return obj.isBlocking
                }

                "cooldown" -> {
                    val material = parse<Material>()
                    if (except("to"))
                        obj.setCooldown(material, parse())
                    else
                        return obj.getCooldown(material)
                }

                "name" -> {
                    return obj.name
                }

                "displayName" -> {
                    return obj.displayName
                }

                "health" -> {
                    if (except("to"))
                        obj.health = parseDouble()
                    else
                        return obj.health
                }

                "maxHealth" -> {
                    if (except("to"))
                        obj.maxHealth = parseDouble()
                    else
                        return obj.maxHealth
                }

                "healthScale" -> {
                    if (except("to"))
                        submit { obj.healthScale = parseDouble() }
                    else
                        return obj.healthScale
                }

                "isSneaking" -> {
                    return obj.isSneaking
                }

                "isSleeping" -> {
                    return obj.isSleeping
                }

                "exp" -> {
                    if (except("to"))
                        obj.exp = parseFloat()
                    else
                        return obj.exp
                }

                "level" -> {
                    if (except("to"))
                        obj.level = parseInt()
                    else
                        return obj.level
                }

                "playSound" -> {
                    val sound = parse<Sound>()
                    var volume = 1f
                    var pitch = 1f
                    if (except("with")) {
                        volume = parseFloat()
                        pitch = parseFloat()
                    }
                    obj.playSound(obj.location, sound, volume, pitch)
                }

                "stopSound" -> {
                    val sound = parse<Sound>()
                    obj.stopSound(sound)
                }

                "velocity" -> {
                    if (except("to"))
                        obj.velocity = parse()
                    else
                        return obj.velocity
                }

                "metadata" -> {
                    val key = parseString()
                    if (except("to"))
                        obj.setMeta(key, parseAny())
                    else
                        return obj.getMetaFirst(key)
                }

                else -> null
            }
        }
    }
}