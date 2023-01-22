package com.skillw.particlelib.pobject

import taboolib.common.platform.function.submit
import taboolib.common.util.Location
import taboolib.common.util.Vector
import com.skillw.particlelib.utils.VectorUtils
import kotlin.math.cos
import kotlin.math.sin

/**
 * 表示一个星星
 *
 * @param origin 起点
 * @param radius 星星半径
 * @param step 步长
 * @author Zoyn
 */
class Star constructor(
    origin: Location,
    val radius: Double = 1.0,
    val step: Double = 0.05,
) : ParticleObject(origin),
    Playable {
    // 每条线的长度
    private val length: Double = cos(Math.toRadians(36.0)) * radius * 2
    private var currentSide = 1
    private var currentStep = 0.0
    private var changeableStart = Vector(1, 0, 0)
    private var changableEnd: Location

    init {
        // 转弧度制
        val radians = Math.toRadians((72 * 2).toDouble())
        val x = radius * cos(radians)
        val y = 0.0
        val z = radius * sin(radians)
        changableEnd = origin.clone().add(x, y, z)
    }

    override fun show() {
        val START = Vector(1, 0, 0)
        // 转弧度制
        val radians = Math.toRadians((72 * 2).toDouble())
        val x = radius * cos(radians)
        val y = 0.0
        val z = radius * sin(radians)
        var end: Location = origin.clone().add(x, y, z)
        for (i in 1..5) {
            var j = 0.0
            while (j < length) {
                val vectorTemp = START.clone().multiply(j)
                val spawnLocation = end.clone().add(vectorTemp)
                spawnParticle(spawnLocation)
                j += step
            }
            val vectorTemp = START.clone().multiply(length)
            end = end.clone().add(vectorTemp)
            VectorUtils.rotateAroundAxisY(START, -144.0)
        }
    }

    override fun play() {
        val START = Vector(1, 0, 0)

        // 转弧度制
        val radians = Math.toRadians((72 * 2).toDouble())
        val x = radius * cos(radians)
        val y = 0.0
        val z = radius * sin(radians)
        var end: Location = origin.clone().add(x, y, z)
        submit(period = period) {
            // 进行关闭
            if (currentSide >= 6) {
                cancel()
                return@submit
            }
            if (currentStep > length) {
                // 切换到下一条边开始
                currentSide += 1
                currentStep = 0.0
                val vectorTemp = START.clone().multiply(length)
                end = end.clone().add(vectorTemp)
                VectorUtils.rotateAroundAxisY(START, -144.0)
            }
            val vectorTemp = START.clone().multiply(currentStep)
            val spawnLocation = end.clone().add(vectorTemp)
            spawnParticle(spawnLocation)
            currentStep += step
        }
    }

    override fun playNextPoint() {
        var vectorTemp = changeableStart.clone().multiply(currentStep)
        val spawnLocation = changableEnd.clone().add(vectorTemp)
        spawnParticle(spawnLocation)
        currentStep += step
        if (currentStep > length) {
            // 切换到下一条边开始
            currentSide += 1
            currentStep = 0.0
            vectorTemp = changeableStart.clone().multiply(length)
            changableEnd = changableEnd.clone().add(vectorTemp)
            VectorUtils.rotateAroundAxisY(changeableStart, -144.0)
        }

        // 重置
        if (currentSide >= 6) {
            currentSide = 1
            currentStep = 0.0
            changeableStart = Vector(1, 0, 0)
            // 转弧度制
            val radians = Math.toRadians((72 * 2).toDouble())
            val x = radius * cos(radians)
            val y = 0.0
            val z = radius * sin(radians)
            changableEnd = origin.clone().add(x, y, z)
        }
    }
}