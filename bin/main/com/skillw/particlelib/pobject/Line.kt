package com.skillw.particlelib.pobject

import taboolib.common.platform.ProxyParticle
import taboolib.common.platform.function.submit
import taboolib.common.platform.sendTo
import taboolib.common.util.Location
import taboolib.common.util.Vector
import java.awt.Color

/**
 * 表示一条线
 *
 * @param start 线的起点
 * @param end 线的终点
 * @param step 每个粒子之间的间隔 (也即步长)
 * @param period 特效周期(如果需要可以使用)
 * @author Zoyn IceCold
 */
class Line constructor(
    private var start: Location,
    private var end: Location,
    /** 步长 */
    private var step: Double = 0.1,
    period: Long = 20L,
) : ParticleObject(start, period), Playable {
    private var vector: Vector? = null

    /** 向量长度 */
    private var length = 0.0
    private var currentStep = 0.0
    /** 构造一个线 */
    /**
     * 构造一个线
     *
     * @param start 线的起点
     * @param end 线的终点
     * @param step 每个粒子之间的间隔 (也即步长)
     */
    init {
        // 对向量进行重置
        resetVector()
    }

    override fun show() {
        var i = 0.0
        while (i < length) {
            val vectorTemp = vector!!.clone().multiply(i)
            spawnParticle(start.clone().add(vectorTemp))
            i += step
        }
    }

    override fun play() {
        submit(period = period) {
            // 进行关闭
            if (currentStep > length) {
                cancel()
                return@submit
            }
            currentStep += step
            val vectorTemp = vector!!.clone().multiply(currentStep)
            spawnParticle(start.clone().add(vectorTemp))
        }
    }

    override fun playNextPoint() {
        currentStep += step
        val vectorTemp = vector!!.clone().multiply(currentStep)
        spawnParticle(start.clone().add(vectorTemp))
        if (currentStep > length) {
            currentStep = 0.0
        }
    }

    fun getStart(): Location {
        return start
    }

    fun setStart(start: Location): Line {
        this.start = start
        resetVector()
        return this
    }

    fun getEnd(): Location {
        return end
    }

    fun setEnd(end: Location): Line {
        this.end = end
        resetVector()
        return this
    }

    fun getStep(): Double {
        return step
    }

    fun setStep(step: Double): Line {
        this.step = step
        resetVector()
        return this
    }

    fun resetVector() {
        vector = end.clone().subtract(start).toVector()
        length = vector!!.length()
        vector!!.normalize()
    }

    companion object {
        fun buildLine(locA: Location, locB: Location, step: Double, particle: ProxyParticle) {
            val vectorAB = locB.clone().subtract(locA).toVector()
            val vectorLength = vectorAB.length()
            vectorAB.normalize()
            var i = 0.0
            while (i < vectorLength) {
                particle.sendTo(locA.clone().add(vectorAB.clone().multiply(i)))
                i += step
            }
        }

        fun buildLine(locA: Location, locB: Location, step: Double, color: Color) {
            val vectorAB = locB.clone().subtract(locA).toVector()
            val vectorLength = vectorAB.length()
            vectorAB.normalize()
            var i = 0.0
            while (i < vectorLength) {
                ProxyParticle.REDSTONE.sendTo(
                    locA.clone().add(vectorAB.clone().multiply(i)),
                    data = ProxyParticle.DustData(color, 1f)
                )
                i += step
            }
        }
    }
}