package com.skillw.particlelib.pobject

import taboolib.common.platform.function.submit
import taboolib.common.util.Location
import java.awt.Color
import java.util.function.Consumer
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * 表示一个球
 *
 * 算法来源:
 * https://stackoverflow.com/questions/9600801/evenly-distributing-n-points-on-a-sphere/26127012#26127012
 *
 * @author Zoyn IceCold
 */
class Sphere constructor(origin: Location, sample: Int = 50, radius: Double = 1.0) : ParticleObject(origin),
    Playable {
    /** 黄金角度 约等于137.5度 */
    private val phi = Math.PI * (3.0 - sqrt(5.0))
    private val locations: MutableList<Location> = ArrayList()
    private var currentSample = 0
    var sample: Int = sample
        set(value) {
            field = value
            resetLocations()
        }
    var radius: Double = radius
        set(value) {
            field = value
            resetLocations()
        }

    /**
     * 构造一个球
     *
     * @param origin 球的圆点
     * @param sample 样本点个数(粒子的数量)
     * @param radius 球的半径
     */
    init {
        resetLocations()
    }

    constructor(origin: Location, sample: Int, radius: Double, color: Color) : this(origin, sample, radius) {
        setColor(color)
    }

    override fun show() {
        locations.forEach(Consumer { loc: Location? -> loc?.let { spawnParticle(it) } })
    }

    override fun play() {
        submit(period = period) {
            // 进行关闭
            if (currentSample + 1 == locations.size) {
                cancel()
                return@submit
            }
            currentSample++
            spawnParticle(locations[currentSample])
        }
    }

    override fun playNextPoint() {
        // 重置
        if (currentSample + 1 == locations.size) {
            currentSample = 0
        }
        spawnParticle(locations[currentSample])
        currentSample++
    }

    fun resetLocations() {
        locations.clear()
        for (i in 0 until sample) {
            // y goes from 1 to -1
            var y = (1 - i / (sample - 1f) * 2).toDouble()
            // radius at y
            val yRadius = sqrt(1 - y * y)
            // golden angle increment
            val theta = phi * i
            val x = cos(theta) * radius * yRadius
            val z = sin(theta) * radius * yRadius
            y *= radius
            locations.add(origin!!.clone().add(x, y, z))
        }
    }
}