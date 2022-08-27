package com.skillw.pouvoir.internal.core.function.action

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.IAction
import com.skillw.pouvoir.api.function.parser.Parser
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
object ActionLocation : IAction {

    override val actions: Set<String>
        get() = hashSetOf(
            "toVector",
            "distance",
            "direction",
            "multiply",
            "length",
            "clone",
            "world",
            "pitch",
            "yaw",
            "x",
            "y",
            "z",
            "add",
            "subtract",
            "zero"
        )
    override val type: Class<*>
        get() = Location::class.java

    override fun action(parser: Parser, obj: Any, action: String): Any? {
        if (obj !is Location) error("$obj is not a Location")
        with(parser) {
            with(obj) {
                return when (action) {
                    "toVector" -> {
                        return toVector()
                    }

                    "distance" -> {
                        return distance(parse())
                    }

                    "direction" -> {
                        if (except("to")) {
                            return parse<Vector>().also { direction = it }
                        }
                        return direction
                    }

                    "multiply" -> {
                        return multiply(parse())
                    }

                    "length" -> {
                        return length()
                    }

                    "clone" -> {
                        return clone()
                    }

                    "world" -> {
                        if (except("to")) {
                            return parse<World>().also { world = it }
                        }
                        return world
                    }

                    "pitch" -> {
                        if (except("to")) {
                            return parseFloat().also { pitch = it }
                        }
                        return pitch
                    }

                    "yaw" -> {
                        if (except("to")) {
                            return parseFloat().also { yaw = it }
                        }
                        return yaw
                    }

                    "x" -> {
                        if (except("to")) {
                            return parseDouble().also { x = it }
                        }
                        return x
                    }

                    "y" -> {
                        if (except("to")) {
                            return parseDouble().also { y = it }
                        }
                        return y
                    }

                    "z" -> {
                        if (except("to")) {
                            return parseDouble().also { z = it }
                        }
                        return z
                    }

                    "add" -> {
                        val any = parseAny()

                        val list = ArrayList<Double>()
                        when (any) {
                            is Location -> any.apply { list[0] = x; list[1] = y; list[2] = z }
                            is Vector -> any.apply { list[0] = x; list[1] = y; list[2] = z }
                            is List<*> -> any.map { it.toString().toDouble() }.also { list.addAll(it) }
                        }
                        return add(list[0], list[1], list[2])
                    }

                    "subtract" -> {
                        val any = parseAny()

                        val list = ArrayList<Double>()
                        when (any) {
                            is Location -> any.apply { list[0] = x; list[1] = y; list[2] = z }
                            is Vector -> any.apply { list[0] = x; list[1] = y; list[2] = z }
                            is List<*> -> any.map { it.toString().toDouble() }.also { list.addAll(it) }
                        }
                        return subtract(list[0], list[1], list[2])
                    }

                    "zero" -> {
                        return zero()
                    }

                    else -> null
                }
            }
        }
    }
}