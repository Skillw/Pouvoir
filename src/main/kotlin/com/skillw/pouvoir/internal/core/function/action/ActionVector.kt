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
object ActionVector : PouAction<Vector>(Vector::class.java) {
    init {
        addExec("rotateAroundX") { vec ->
            vec.rotateAroundX(parse())
        }

        addExec("rotateAroundY") { vec ->
            vec.rotateAroundY(parse())
        }

        addExec("rotateAroundZ") { vec ->
            vec.rotateAroundZ(parse())
        }

        addExec("toLocation") { vec ->
            val world = parse<World>()
            var yaw = 0.0f
            var pitch = 0.0f
            if (except("with")) {
                yaw = parse()
                pitch = parse()
            }
            vec.toLocation(world, yaw, pitch)
        }

        addExec("normalize") { vec ->
            vec.normalize()
        }

        addExec("isNormalized") { vec ->
            vec.isNormalized
        }

        addExec("isInSphere") { vec ->
            vec.isInSphere(parse(), parse())
        }

        addExec("isInAABB") { vec ->
            vec.isInAABB(parse(), parse())
        }

        addExec("midpoint") { vec ->
            vec.getMidpoint(parse())
        }

        addExec("dot") { vec ->
            vec.dot(parse())
        }

        addExec("divide") { vec ->
            vec.divide(parse())
        }

        addExec("distance") { vec ->
            vec.distance(parse())
        }

        addExec("crossProduct") { vec ->
            vec.crossProduct(parse())
        }

        addExec("copy") { vec ->
            vec.copy(parse())
        }

        addExec("angle") { vec ->
            vec.angle(parse())
        }

        addExec("multiply") { vec ->
            vec.multiply(parseDouble())
        }

        addExec("length") { vec ->
            vec.length()
        }

        addExec("clone") { vec ->
            clone()
        }

        addExec("x") { vec ->
            if (except("to")) {
                parseDouble().also { vec.x = it }
            }
            vec.x
        }

        addExec("y") { vec ->
            if (except("to")) {
                parseDouble().also { vec.y = it }
            }
            vec.y
        }

        addExec("z") { vec ->
            if (except("to")) {
                parseDouble().also { vec.z = it }
            }
            vec.z
        }

        addExec("add") { vec ->
            val any = parseAny()

            val list = ArrayList<Double>()
            when (any) {
                is Location -> any.apply { list[0] = x; list[1] = y; list[2] = z }
                is Vector -> any.apply { list[0] = x; list[1] = y; list[2] = z }
                is List<*> -> any.map { it.toString().toDouble() }.also { list.addAll(it) }
            }
            vec.add(Vector(list[0], list[1], list[2]))
        }

        addExec("subtract") { vec ->
            val any = parseAny()

            val list = ArrayList<Double>()
            when (any) {
                is Location -> any.apply { list[0] = x; list[1] = y; list[2] = z }
                is Vector -> any.apply { list[0] = x; list[1] = y; list[2] = z }
                is List<*> -> any.map { it.toString().toDouble() }.also { list.addAll(it) }
            }
            vec.subtract(Vector(list[0], list[1], list[2]))
        }

        addExec("zero") { vec ->
            vec.zero()
        }
    }
}