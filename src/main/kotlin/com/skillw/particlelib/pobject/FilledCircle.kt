package com.skillw.particlelib.pobject

import taboolib.common.platform.function.submit
import taboolib.common.platform.function.submitAsync
import taboolib.common.util.Location
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * 表示一个实心圆
 *
 * @param origin 原点坐标
 * @param radius 半径大小
 * @param sample 粒子数量
 * @author Zoyn
 */
class FilledCircle(origin: Location, val radius: Double, private val sample: Int) : ParticleObject(origin), Playable {

    // 动画中当前的第 currentCount 个粒子
    private var currentCount: Int = 0

    override fun show() {
        for (i in 0 until sample) {
            once(i)
        }
    }

    private fun once(i: Number) {
        val indices = i.toLong() + 0.5
        val r = sqrt(indices / sample)
        val theta = Math.PI * (1 + sqrt(5.0)) * indices
        val x = radius * r * cos(theta)
        val z = radius * r * sin(theta)
        val spawnLocation: Location = origin.clone().add(x, 0.0, z)
        spawnParticle(spawnLocation)
    }

    /**
     * 获得实心圆中所有点的Location
     *
     * @param origin 原点
     * @param count 个数
     * @return 粒子播放的点
     */
    fun calculateLocations(origin: Location, count: Long): List<Location> {
        val locations: MutableList<Location> = ArrayList()
        for (i in 0 until count) {
            once(i)
        }
        return locations
    }

    override fun play() {
        submit(period = period) {
            if (currentCount > sample) {
                cancel()
                return@submit
            }
            currentCount++
            once(currentCount)
        }
    }

    override fun playNextPoint() {
        currentCount++
        once(currentCount)

        // 进行重置
        if (currentCount > sample) {
            currentCount = 0
        }
    }

    /**
     * 使用给定的时间播放粒子
     *
     * @param time 持续时间, 单位 tick
     * @param count 粒子数量
     */
    fun playWithTime(time: Long, count: Long) {
        if (time == 0L) {
            for (i in 0 until count) {
                once(i)
            }
            return
        }
        // 这里用来计量当前要播放的粒子是第几个tick, 也可说是帧数
        var frame = 0
        var sample = 0
        submitAsync(period = 1) {
            if (frame >= time) {
                cancel()
                return@submitAsync
            }
            frame++
            // 每一帧要计算的粒子数量
            val frameTick = (count / time).toInt()
            var i = sample.toDouble()
            while (i < (frame + 1) * frameTick) {
                once(i)
                i += 1.0
            }
            sample += frameTick
        }
    }
}