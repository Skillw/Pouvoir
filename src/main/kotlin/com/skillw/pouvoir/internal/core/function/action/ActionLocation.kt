package com.skillw.pouvoir.internal.core.function.action

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.PouAction
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector

/**
 * @className ActionArray
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object ActionLocation : PouAction<Location>(Location::class.java) {
    init {
        addExec("toVector") { loc ->
            loc.toVector()
        }

        addExec("distance") { loc ->
            loc.distance(parse())
        }

        addExec("direction") { loc ->
            if (except("to")) {
                parse<Vector>().also { loc.direction = it }
            }
            loc.direction
        }

        addExec("multiply") { loc ->
            loc.multiply(parse())
        }

        addExec("length") { loc ->
            loc.length()
        }

        addExec("clone") { loc ->
            clone()
        }

        addExec("world") { loc ->
            if (except("to")) {
                parse<World>().also { loc.world = it }
            }
            loc.world
        }

        addExec("pitch") { loc ->
            if (except("to")) {
                parseFloat().also { loc.pitch = it }
            }
            loc.pitch
        }

        addExec("yaw") { loc ->
            if (except("to")) {
                parseFloat().also { loc.yaw = it }
            }
            loc.yaw
        }

        addExec("x") { loc ->
            if (except("to")) {
                parseDouble().also { loc.x = it }
            }
            loc.x
        }

        addExec("y") { loc ->
            if (except("to")) {
                parseDouble().also { loc.y = it }
            }
            loc.y
        }

        addExec("z") { loc ->
            if (except("to")) {
                parseDouble().also { loc.z = it }
            }
            loc.z
        }

        addExec("add") { loc ->
            val any = parseAny()

            val list = ArrayList<Double>()
            when (any) {
                is Location -> any.apply { list[0] = x; list[1] = y; list[2] = z }
                is Vector -> any.apply { list[0] = x; list[1] = y; list[2] = z }
                is List<*> -> any.map { it.toString().toDouble() }.also { list.addAll(it) }
            }
            loc.add(list[0], list[1], list[2])
        }

        addExec("subtract") { loc ->
            val any = parseAny()

            val list = ArrayList<Double>()
            when (any) {
                is Location -> any.apply { list[0] = x; list[1] = y; list[2] = z }
                is Vector -> any.apply { list[0] = x; list[1] = y; list[2] = z }
                is List<*> -> any.map { it.toString().toDouble() }.also { list.addAll(it) }
            }
            loc.subtract(list[0], list[1], list[2])
        }

        addExec("zero") { loc ->
            loc.zero()
        }
    }
}