package com.skillw.pouvoir.internal.core.asahi.infix.bukkit

import com.skillw.asahi.api.annotation.AsahiInfix
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix
import com.skillw.pouvoir.util.angleFixed
import com.skillw.pouvoir.util.rotateAroundX
import com.skillw.pouvoir.util.rotateAroundY
import com.skillw.pouvoir.util.rotateAroundZ
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector

/**
 * @className ActionVector
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AsahiInfix
object InfixVector : BaseInfix<Vector>(Vector::class.java, "bukkit") {
    init {
        infix("rotateAroundX") { vec ->
            vec.rotateAroundX(parse())
        }

        infix("rotateAroundY") { vec ->
            vec.rotateAroundY(parse())
        }

        infix("rotateAroundZ") { vec ->
            vec.rotateAroundZ(parse())
        }

        infix("toLocation") { vec ->
            val world = parse<World>()
            var yaw = 0.0f
            var pitch = 0.0f
            if (expect("with")) {
                yaw = parse()
                pitch = parse()
            }
            vec.toLocation(world, yaw, pitch)
        }

        infix("normalize") { vec ->
            vec.normalize()
        }

        infix("isNormalized") { vec ->
            vec.length() == 1.0
        }

        infix("isInSphere") { vec ->
            vec.isInSphere(parse(), parse())
        }

        infix("isInAABB") { vec ->
            vec.isInAABB(parse(), parse())
        }

        infix("midpoint") { vec ->
            vec.getMidpoint(parse())
        }

        infix("dot") { vec ->
            vec.dot(parse())
        }

        infix("divide") { vec ->
            vec.divide(parse())
        }

        infix("distance") { vec ->
            vec.distance(parse())
        }

        infix("crossProduct") { vec ->
            vec.crossProduct(parse())
        }

        infix("copy") { vec ->
            vec.copy(parse())
        }

        infix("angle") { vec ->
            vec.angleFixed(parse())
        }

        infix("multiply") { vec ->
            vec.multiply(parse<Double>())
        }

        infix("length") { vec ->
            vec.length()
        }

        infix("clone") { vec ->
            vec.clone()
        }

        infix("x") { vec ->
            if (expect("to")) {
                parse<Double>().also { vec.x = it }
            }
            vec.x
        }

        infix("y") { vec ->
            if (expect("to")) {
                parse<Double>().also { vec.y = it }
            }
            vec.y
        }

        infix("z") { vec ->
            if (expect("to")) {
                parse<Double>().also { vec.z = it }
            }
            vec.z
        }

        infix("add") { vec ->
            val any = if (peek() == "[") parseList() else parse<Any>()

            val list = ArrayList<Double>()
            when (any) {
                is Location -> any.apply { list[0] = x; list[1] = y; list[2] = z }
                is Vector -> any.apply { list[0] = x; list[1] = y; list[2] = z }
                is List<*> -> any.map { it.toString().toDouble() }.also { list.addAll(it) }
            }
            vec.add(Vector(list[0], list[1], list[2]))
        }

        infix("subtract") { vec ->
            val any = parse<Any>()

            val list = ArrayList<Double>()
            when (any) {
                is Location -> any.apply { list[0] = x; list[1] = y; list[2] = z }
                is Vector -> any.apply { list[0] = x; list[1] = y; list[2] = z }
                is List<*> -> any.map { it.toString().toDouble() }.also { list.addAll(it) }
            }
            vec.subtract(Vector(list[0], list[1], list[2]))
        }

        infix("zero") { vec ->
            vec.zero()
        }
    }
}