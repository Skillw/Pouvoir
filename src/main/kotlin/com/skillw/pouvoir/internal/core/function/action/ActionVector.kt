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
object ActionVector : IAction {

    override val actions: Set<String>
        get() = hashSetOf(
            "rotateAroundX",
            "rotateAroundY",
            "rotateAroundZ",
            "toLocation",
            "normalize",
            "isNormalized",
            "isInAABB",
            "isInSphere",
            "midpoint",
            "dot",
            "divide",
            "distance",
            "crossProduct",
            "copy",
            "angle",
            "multiply",
            "length",
            "clone",
            "x",
            "y",
            "z",
            "add",
            "subtract",
            "zero"
        )
    override val type: Class<*>
        get() = Vector::class.java

    override fun action(parser: Parser, obj: Any, action: String): Any? {
        if (obj !is Vector) error("$obj is not a Location")
        with(parser) {
            with(obj) {
                return when (action) {
                    "rotateAroundX" -> {
                        return rotateAroundX(parse())
                    }

                    "rotateAroundY" -> {
                        return rotateAroundY(parse())
                    }

                    "rotateAroundZ" -> {
                        return rotateAroundZ(parse())
                    }

                    "toLocation" -> {
                        val world = parse<World>()
                        var yaw = 0.0f
                        var pitch = 0.0f
                        if (except("with")) {
                            yaw = parse()
                            pitch = parse()
                        }
                        return toLocation(world, yaw, pitch)
                    }

                    "normalize" -> {
                        return normalize()
                    }

                    "isNormalized" -> {
                        return isNormalized
                    }

                    "isInSphere" -> {
                        return isInSphere(parse(), parse())
                    }

                    "isInAABB" -> {
                        return isInAABB(parse(), parse())
                    }

                    "midpoint" -> {
                        return getMidpoint(parse())
                    }

                    "dot" -> {
                        return dot(parse())
                    }

                    "divide" -> {
                        return divide(parse())
                    }

                    "distance" -> {
                        return distance(parse())
                    }

                    "crossProduct" -> {
                        return crossProduct(parse())
                    }

                    "copy" -> {
                        return copy(parse())
                    }

                    "angle" -> {
                        return angle(parse())
                    }

                    "multiply" -> {
                        return multiply(parseDouble())
                    }

                    "length" -> {
                        return length()
                    }

                    "clone" -> {
                        return clone()
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
                        return add(Vector(list[0], list[1], list[2]))
                    }

                    "subtract" -> {
                        val any = parseAny()

                        val list = ArrayList<Double>()
                        when (any) {
                            is Location -> any.apply { list[0] = x; list[1] = y; list[2] = z }
                            is Vector -> any.apply { list[0] = x; list[1] = y; list[2] = z }
                            is List<*> -> any.map { it.toString().toDouble() }.also { list.addAll(it) }
                        }
                        return subtract(Vector(list[0], list[1], list[2]))
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