package com.skillw.particlelib.pobject

import taboolib.common.util.Location
import com.skillw.particlelib.utils.LocationUtils
import kotlin.math.*

/**
 * 一朵莲花
 *
 * @constructor
 */
class Lotus(origin: Location) : ParticleObject(origin) {
    override fun show() {
        // 外围花瓣
        run {
            var t = -0.15
            while (t <= 0.15) {
                val x = 5 * sin(t) * cos(t) * ln(abs(t))
                var y = 5 * sqrt(abs(t)) * cos(t)
                y -= 5.0
                val spawn = origin.clone().add(x, 0.0, y)
                for (i in 0..8) {
                    val temp = LocationUtils.rotateLocationAboutPoint(spawn, 360.0 / 8.0 * i, origin)
                    spawnParticle(temp)
                }
                spawnParticle(spawn)
                t += 0.005
            }
        }

        // 内圈花瓣
        run {
            var t = -0.2
            while (t <= 0.2) {
                val x = 3 * sin(t) * cos(t) * ln(abs(t))
                var y = 3 * sqrt(abs(t)) * cos(t)
                y -= 3.65
                val spawn = LocationUtils.rotateLocationAboutPoint(origin.clone().add(x, 0.0, y), 22.0, origin)
                for (i in 0..8) {
                    val temp = LocationUtils.rotateLocationAboutPoint(spawn, 360.0 / 8.0 * i, origin)
                    spawnParticle(temp)
                }
                spawnParticle(spawn)
                t += 0.01
            }
        }

        // 最外层小花瓣
        var t = -0.1
        while (t <= 0.1) {
            val x = 2 * sin(t) * cos(t) * ln(abs(t))
            var y = 2 * sqrt(abs(t)) * cos(t)
            y -= 4.6
            val spawn = LocationUtils.rotateLocationAboutPoint(origin.clone().add(x, 0.0, y), 22.0, origin)
            for (i in 0..8) {
                val temp = LocationUtils.rotateLocationAboutPoint(spawn, 360.0 / 8.0 * i, origin)
                spawnParticle(temp)
            }
            spawnParticle(spawn)
            t += 0.01
        }
    }
}