package com.skillw.pouvoir.internal.core.asahi.prefix.bukkit

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector

/**
 * @className World
 *
 * @author Glom
 * @date 2023/1/14 0:33 Copyright 2023 user. All rights reserved.
 */

@AsahiPrefix(["world"])
private fun world() = prefixParser<World> {
    val getter = quest<String>()
    result {
        val name = getter.get()
        Bukkit.getWorld(name) ?: error("World not found: $name")
    }
}

@AsahiPrefix(["location"])
private fun location() = prefixParser<Location> {
    expect("[")
    val world = quest<World>()
    val x = quest<Double>()
    val y = quest<Double>()
    val z = quest<Double>()
    expect("]")
    var yaw = quester { 0.0f }
    var pitch = quester { 0.0f }
    if (expect("with")) {
        yaw = quest()
        pitch = quest()
    }
    result { Location(world.get(), x.get(), y.get(), z.get(), yaw.get(), pitch.get()) }
}

@AsahiPrefix(["vector"])
private fun vector() = prefixParser<Vector> {
    expect("[")
    val x = quest<Double>()
    val y = quest<Double>()
    val z = quest<Double>()
    expect("]")
    result { Vector(x.get(), y.get(), z.get()) }
}