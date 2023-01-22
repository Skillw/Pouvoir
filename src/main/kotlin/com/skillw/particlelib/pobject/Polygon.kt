package com.skillw.particlelib.pobject

import taboolib.common.platform.function.submit
import taboolib.common.util.Location
import taboolib.common.util.Vector
import com.skillw.particlelib.utils.VectorUtils
import kotlin.math.cos
import kotlin.math.sin

/**
 * 表示一个多边形
 *
 * @param side 边数
 * @param origin 原点
 */
class Polygon constructor(
    side: Int, origin: Location, step: Double = 0.02, radius: Double = 5.0,
) : ParticleObject(origin),
    Playable {
    private val locations: MutableList<Location> = ArrayList()

    /** 边数 */
    var side: Int = side
        set(value) {
            field = value
            resetLocations()
        }
    var step: Double = step
        set(value) {
            field = value
            resetLocations()
        }
    var radius: Double = radius
        set(value) {
            field = value
            resetLocations()
        }
    private lateinit var currentVector: Vector
    private var currentLoc = 0
    private var length = 0.0
    private var currentStep = 0.0

    /** 构造一个多边形 */
    init {
        require(side > 2) { "边数不可为小于或等于2的数!" }
        resetLocations()
    }

    override fun show() {
        if (locations.isEmpty()) {
            return
        }
        for (i in locations.indices) {
            if (i + 1 == locations.size) {
                buildLine(locations[i], locations[0], step)
                break
            }
            buildLine(locations[i], locations[i + 1], step)
        }
    }

    private fun once() {
        val vectorTemp = currentVector.clone().normalize().multiply(currentStep)
        spawnParticle(locations[currentLoc].clone().add(vectorTemp))

        // 重置
        if (currentStep > length) {
            currentStep = 0.0
            currentVector = VectorUtils.rotateAroundAxisY(currentVector, 360.0 / side)
            currentLoc++
        }
    }

    override fun play() {
        submit(period = period) {
            once()
            // 在此处进行退出
            if (currentLoc == side) {
                cancel()
                return@submit
            }
            currentStep += step
        }
    }

    override fun playNextPoint() {
        once()
        if (currentLoc == side) {
            currentLoc = 0
        }
        currentStep += step
    }

    fun resetLocations() {
        locations.clear()
        var angle = 0.0
        while (angle <= 360) {
            val radians = Math.toRadians(angle)
            val x = cos(radians) * radius
            val z = sin(radians) * radius
            locations.add(origin.clone().add(x, 0.0, z))
            angle += 360.0 / side
        }
        currentVector = locations[1].clone().subtract(locations[0]).toVector()
        length = currentVector.length()
    }

    /**
     * 此方法只用于本 Polygon
     *
     * @param locA 点A
     * @param locB 点B
     * @param step 步长
     */
    private fun buildLine(locA: Location, locB: Location, step: Double) {
        val vectorAB = locB.clone().subtract(locA).toVector()
        val vectorLength = vectorAB.length()
        vectorAB.normalize()
        var i = 0.0
        while (i < vectorLength) {
            spawnParticle(locA.clone().add(vectorAB.clone().multiply(i)))
            i += step
        }
    }
}