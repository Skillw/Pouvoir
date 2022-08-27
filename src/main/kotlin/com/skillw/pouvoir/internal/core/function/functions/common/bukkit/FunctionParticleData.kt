package com.skillw.pouvoir.internal.core.function.functions.common.bukkit

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import taboolib.common.platform.ProxyParticle
import taboolib.common.util.Location
import java.awt.Color

/**
 * @className FunctionBlock
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionParticleData : PouFunction<ProxyParticle.Data>(
    "particleData"
) {
    override fun execute(parser: Parser): ProxyParticle.Data {
        with(parser) {
            val token = parseString()
            except("[")
            return when (token) {
                "dust" -> parseDustData()
                "dustTransition" -> parseDustTransitionData()
                "block" -> parseBlockData()
                "item" -> parseItemData()
                "vibration" -> parseVibrationData()
                else -> error("Wrong Particle Data Type $token")
            }.also {
                except("]")
            }
        }
    }

    private fun Parser.parseDustData(): ProxyParticle.DustData {
        return ProxyParticle.DustData(parse<Color>().also { except("in") }, parse())
    }

    private fun Parser.parseDustTransitionData(): ProxyParticle.DustTransitionData {
        return ProxyParticle.DustTransitionData(
            parse<Color>().also { except("to") },
            parse<Color>().also { except("in") },
            parse()
        )
    }

    private fun Parser.parseBlockData(): ProxyParticle.BlockData {
        return ProxyParticle.BlockData(parseString(), kotlin.run { if (except("with")) parseInt() else 0 })
    }

    private fun Parser.parseItemData(): ProxyParticle.ItemData {
        val material = parseString()
        var data = 0
        var name = ""
        var lore: List<String> = emptyList()
        var customModelData: Int = -1
        if (except("with")) {
            data = parse()
        }
        if (except("name")) {
            name = parse()
        }
        if (except("lore")) {
            lore = parseList().map { it.toString() }
        }
        if (except("data")) {
            customModelData = parseInt()
        }
        return ProxyParticle.ItemData(material, data, name, lore, customModelData)
    }

    private fun Parser.parseVibrationData(): ProxyParticle.VibrationData {
        return ProxyParticle.VibrationData(
            parse<Location>().also { except("to") },
            parse<ProxyParticle.VibrationData.Destination>().also { except("in") },
            parse()
        )
    }
}