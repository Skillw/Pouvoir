package com.skillw.pouvoir.internal.core.asahi.prefix.bukkit.particle

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import org.bukkit.Location
import taboolib.common.platform.ProxyParticle
import taboolib.common.platform.sendTo
import taboolib.common.util.Vector
import taboolib.platform.util.toProxyLocation
import java.awt.Color

/**
 * @className Effect
 *
 * @author Glom
 * @date 2023/1/14 0:13 Copyright 2023 user. All rights reserved.
 */

@AsahiPrefix(["effect"])
private fun effect() = prefixParser {
    val particle = quest<ProxyParticle>()
    expect("at")
    val location = quest<Location>()
    var range = quester { 15.0 }
    var offset = quester { Vector(0, 0, 0) }
    var count = quester { 1 }
    var speed = quester { 1.0 }
    var data: Quester<ProxyParticle.Data>? = null
    if (expect("limit")) {
        range = quest()
    }
    if (expect("offset")) {
        offset = quest()
    }
    if (expect("amount")) {
        count = quest()
    }
    if (expect("speed")) {
        speed = quest()
    }
    if (expect("data")) {
        data = quest()
    }
    result {
        particle.get()
            .sendTo(
                location.get().toProxyLocation(),
                range.get(),
                offset.get(),
                count.get(),
                speed.get(),
                data?.get()
            )
    }
}

@AsahiPrefix(["color"])
private fun color() = prefixParser {
    expect("[")
    val r = quest<Int>()
    val g = quest<Int>()
    val b = quest<Int>()
    expect("]")
    result { Color(r.get(), g.get(), b.get()) }
}

@AsahiPrefix
private fun particle() = prefixParser {
    val particle = quest<ProxyParticle>()
    result {
        particle.get()
    }
}