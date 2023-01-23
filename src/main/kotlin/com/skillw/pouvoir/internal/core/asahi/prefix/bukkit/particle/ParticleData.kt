package com.skillw.pouvoir.internal.core.asahi.prefix.bukkit.particle

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.member.parser.prefix.namespacing.PrefixParser
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import org.bukkit.entity.Entity
import taboolib.common.platform.ProxyParticle
import taboolib.common.util.Location
import taboolib.common5.cint
import taboolib.platform.util.toProxyLocation
import java.awt.Color

/**
 * @className FunctionBlock
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */

@AsahiPrefix(["particleData"])
fun particleData() = prefixParser {
    val token = next()
    expect("[", "{")
    when (token) {
        "dust" -> questDustData()
        "dustTransition" -> questDustTransitionData()
        "block" -> questBlockData()
        "item" -> questItemData()
        "vibration" -> questVibrationData()
        else -> error("Wrong Particle Data Type $token")
    }.also {
        expect("]", "}")
    } as Quester<ProxyParticle.Data>
}

private fun PrefixParser<*>.questDustData(): Quester<ProxyParticle.DustData> {
    val color = quest<Color>()
    expect("in")
    val size = quest<Float>()
    return result { ProxyParticle.DustData(color.get(), size.get()) }
}

private fun PrefixParser<*>.questDustTransitionData(): Quester<ProxyParticle.DustTransitionData> {
    val from = quest<Color>()
    expect("to")
    val to = quest<Color>()
    expect("in")
    val size = quest<Float>()
    return result {
        ProxyParticle.DustTransitionData(from.get(), to.get(), size.get())
    }
}

private fun PrefixParser<*>.questBlockData(): Quester<ProxyParticle.BlockData> {
    val material = quest<String>()
    val data = if (expect("with")) quest() else quester { 0 }
    return result { ProxyParticle.BlockData(material.get(), data.get()) }
}

private fun PrefixParser<*>.questItemData(): Quester<ProxyParticle.ItemData> {
    val material = quest<String>()
    val map = if (peek() == "[" || peek() == "{") questTypeMap() else quester { emptyMap<String, Any>() }
    val data = map.quester { it.getOrDefault("data", 0).cint }
    val name = map.quester { it.getOrDefault("name", material.get()).toString() }
    val lore = map.quester { it.getOrDefault("lore", emptyList<String>()) as List<String> }
    val customModelData = map.quester { it.getOrDefault("data", -1).cint }
    return result {
        ProxyParticle.ItemData(
            material.get(),
            data.get(),
            name.get(),
            lore.get(),
            customModelData.get()
        )
    }
}

@AsahiPrefix
fun destination() = prefixParser {
    when (val token = next()) {
        "location" -> {
            val loc = quest<org.bukkit.Location>()
            result { ProxyParticle.VibrationData.LocationDestination(loc.get().toProxyLocation()) }
        }

        "entity" -> {
            val entity = quest<Entity>()
            result { ProxyParticle.VibrationData.EntityDestination(entity.get().uniqueId) }
        }

        else -> error("Wrong Destination Type $token")
    }
}

private fun PrefixParser<*>.questVibrationData(): Quester<ProxyParticle.VibrationData> {
    val from = quest<Location>()
    expect("to")
    val destination = quest<ProxyParticle.VibrationData.Destination>()
    expect("in")
    val time = quest<Int>()
    return result {
        ProxyParticle.VibrationData(from.get(), destination.get(), time.get())
    }
}