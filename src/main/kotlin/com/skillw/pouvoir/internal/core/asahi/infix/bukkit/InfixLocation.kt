package com.skillw.pouvoir.internal.core.asahi.infix.bukkit

import com.skillw.asahi.api.annotation.AsahiInfix
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector

/**
 * @className ActionLocation
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AsahiInfix
object InfixLocation : BaseInfix<Location>(Location::class.java, "bukkit") {
    init {
        infix("toVector") { loc ->
            loc.toVector()
        }

        infix("distance") { loc ->
            loc.distance(parse())
        }

        infix("direction") { loc ->
            if (expect("to")) {
                parse<Vector>().also { loc.direction = it }
            }
            loc.direction
        }

        infix("multiply") { loc ->
            loc.multiply(parse())
        }

        infix("length") { loc ->
            loc.length()
        }

        infix("clone") { loc ->
            loc.clone()
        }

        infix("world") { loc ->
            if (expect("to")) {
                parse<World>().also { loc.world = it }
            }
            loc.world
        }

        infix("pitch") { loc ->
            if (expect("to")) {
                parse<Float>().also { loc.pitch = it }
            }
            loc.pitch
        }

        infix("yaw") { loc ->
            if (expect("to")) {
                parse<Float>().also { loc.yaw = it }
            }
            loc.yaw
        }

        infix("x") { loc ->
            if (expect("to")) {
                parse<Double>().also { loc.x = it }
            }
            loc.x
        }

        infix("y") { loc ->
            if (expect("to")) {
                parse<Double>().also { loc.y = it }
            }
            loc.y
        }

        infix("z") { loc ->
            if (expect("to")) {
                parse<Double>().also { loc.z = it }
            }
            loc.z
        }

        infix("add") { loc ->
            val any = if (peek() == "[") parseList() else parse<Any>()

            val list = ArrayList<Double>()
            when (any) {
                is Location -> any.apply { list[0] = x; list[1] = y; list[2] = z }
                is Vector -> any.apply { list[0] = x; list[1] = y; list[2] = z }
                is List<*> -> any.map { it.toString().toDouble() }.also { list.addAll(it) }
            }
            loc.add(list[0], list[1], list[2])
        }

        infix("subtract") { loc ->
            val any = parse<Any>()

            val list = ArrayList<Double>()
            when (any) {
                is Location -> any.apply { list[0] = x; list[1] = y; list[2] = z }
                is Vector -> any.apply { list[0] = x; list[1] = y; list[2] = z }
                is List<*> -> any.map { it.toString().toDouble() }.also { list.addAll(it) }
            }
            loc.subtract(list[0], list[1], list[2])
        }

        infix("zero") { loc ->
            loc.zero()
        }
    }
}