package com.skillw.particlelib.pobject

import taboolib.common.util.Location
import taboolib.common.util.Vector
import com.skillw.particlelib.utils.VectorUtils


/**
 * 表示一个翅膀
 *
 * @author Zoyn
 */
class Wing
/**
 * 构造一个翅膀
 *
 * @param origin 起点
 * @param pattern 图案
 * @param minRotAngle 起始扇动角度（最小
 * @param maxRotAngle 最大扇动角度（最大
 * @param interval 粒子之间的距离
 * @constructor
 */
constructor(
    origin: Location,
    pattern: List<String>,
    minRotAngle: Double = 30.0,
    maxRotAngle: Double = 60.0,
    interval: Double = 0.2,
) : ParticleObject(origin) {
    /** 用于缓存所有的点的向量 */
    private val vectors: MutableList<Vector> = ArrayList()

    /** 翅膀的图案 */
    private var pattern: List<String> = pattern
        set(value) {
            field = value
            resetWing()
        }

    /** 粒子之间的距离 */
    private var interval: Double = interval

    /** 最小起始旋转角 */
    private var minRotAngle: Double = -minRotAngle

    /** 最大旋转角 */
    private var maxRotAngle: Double = -maxRotAngle

    /** 翅膀是否要进行旋转 */
    private var swing: Boolean = true
    private var currentAngle: Double = -minRotAngle
    private var increase: Boolean = false

    init {
        resetWing()
    }

    override fun show() {
        for (vector in vectors) {
            if (entity != null) {
                this.spawnParticle(
                    origin.clone().add(
                        VectorUtils.rotateVector(
                            vector, origin
                                .yaw - 90 + currentAngle.toFloat(), 0f
                        )
                    )
                )
                this.spawnParticle(
                    origin.clone().add(
                        VectorUtils.rotateVector(
                            vector.clone().setX(-vector.x), origin
                                .yaw - 90 - currentAngle.toFloat(), 0f
                        )
                    )
                )
                continue
            }
            this.spawnParticle(
                origin.clone().add(VectorUtils.rotateVector(vector, currentAngle.toFloat(), 0f))
            )
            this.spawnParticle(
                origin.clone()
                    .add(VectorUtils.rotateVector(vector.clone().setX(-vector.x), -currentAngle.toFloat(), 0f))
            )
        }
        if (!swing) {
            return
        }
        if (!increase) {
            currentAngle--
        } else {
            currentAngle++
        }
        if (currentAngle >= minRotAngle) {
            increase = false
        }
        if (currentAngle <= maxRotAngle) {
            increase = true
        }
    }

    /** 利用图案来计算出每个粒子的向量 */
    fun resetWing() {
        vectors.clear()
        for (i in pattern.indices) {
            val line = pattern[i]
            val chars = line.toCharArray()
            for (j in chars.indices) {
                val c = chars[j]
                if (!Character.isWhitespace(c)) {
                    val x = interval * (j + 1)
                    val y = interval * (pattern.size - i)
                    val vector = Vector(x, y, 0.0)
                    vectors.add(vector)
                }
            }
        }
    }
}