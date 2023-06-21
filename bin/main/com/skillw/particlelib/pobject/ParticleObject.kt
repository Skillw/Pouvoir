package com.skillw.particlelib.pobject

import com.skillw.particlelib.utils.matrix.Matrix
import com.skillw.pouvoir.api.feature.selector.Target
import com.skillw.pouvoir.api.feature.selector.toTarget
import com.skillw.pouvoir.util.getEntities
import org.bukkit.entity.Entity
import taboolib.common.platform.ProxyParticle
import taboolib.common.platform.function.submit
import taboolib.common.platform.function.submitAsync
import taboolib.common.platform.sendTo
import taboolib.common.platform.service.PlatformExecutor
import taboolib.common.util.Location
import taboolib.common.util.Vector
import taboolib.platform.util.toProxyLocation
import java.awt.Color
import java.util.*

/**
 * 表示一个特效对象
 *
 * @author Zoyn IceCold
 */
abstract class ParticleObject() {
    constructor(origin: Location) : this() {
        this.origin = origin
    }

    constructor(origin: Location, period: Long) : this() {
        this.origin = origin
        this.period = period
    }

    var origin: Location = Location("", 0.0, 0.0, 0.0)
        get() {
            return if (entity != null) {
                entity!!.location.toProxyLocation()
            } else field
        }
    var period: Long = 0
    var showType = ShowType.NONE
    private var running = false
    var particle = ProxyParticle.VILLAGER_HAPPY
    var count = 1
    var offset = Vector(0, 0, 0)
    var extra = 0.0
    private var data: ProxyParticle.Data? = null
    var entity: Entity? = null
    private var task: PlatformExecutor.PlatformTask? = null

    /** 变化量 */
    var incrementX = 0.0
    var incrementY = 0.0
    var incrementZ = 0.0

    /** 表示该特效对象所拥有的矩阵 */
    private var matrix: Matrix? = null


    /** 粒子命中目标容器 */
    private var calcHit = false
    private var hitTargets = LinkedList<Target>()
    private var onHit: ((Target) -> Unit)? = null

    fun calcHitTargets() {
        calcHit = true
    }

    fun onHit(onHit: (Target) -> Unit) {
        this.onHit = onHit
    }

    abstract fun show()
    fun alwaysShow() {
        turnOffTask()

        // 此处的延迟 2tick 是为了防止turnOffTask还没把特效给关闭时的缓冲
        submit(delay = 2L) {
            running = true
            task = submit(period = period) {
                if (!running) cancel()
                show()
            }
            showType = (ShowType.ALWAYS_SHOW)
        }
    }

    fun alwaysShowAsync() {
        turnOffTask()

        // 此处的延迟 2tick 是为了防止turnOffTask还没把特效给关闭时的缓冲
        submit(delay = 2L) {
            running = true
            task = submitAsync(period = period) {
                if (!running) cancel()
                show()
            }
            showType = (ShowType.ALWAYS_SHOW_ASYNC)
        }
    }

    fun alwaysPlay() {
        if (this !is Playable) {
            try {
                throw NoSuchMethodException("该对象不支持播放!")
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            }
        }
        val playable = this as Playable
        turnOffTask()

        // 此处的延迟 2tick 是为了防止turnOffTask还没把特效给关闭时的缓冲
        submit(delay = 2L) {
            running = true
            task = submit(period = period) {
                if (!running) cancel()
                playable.playNextPoint()
            }
            showType = (ShowType.ALWAYS_PLAY)
        }
    }

    fun alwaysPlayAsync() {
        if (this !is Playable) {
            try {
                throw NoSuchMethodException("该对象不支持播放!")
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            }
        }
        val playable = this as Playable
        turnOffTask()

        // 此处的延迟 2tick 是为了防止turnOffTask还没把特效给关闭时的缓冲
        submit(delay = 2L) {
            running = true
            task = submitAsync(period = period) {
                if (!running) cancel()
                playable.playNextPoint()
            }
            showType = (ShowType.ALWAYS_PLAY_ASYNC)
        }
    }

    fun turnOffTask() {
        task?.let {
            running = false
            it.cancel()
            showType = (ShowType.NONE)
        }
    }

    fun addMatrix(matrix: Matrix): ParticleObject {
        if (this.matrix == null) {
            setMatrix(matrix)
            return this
        }
        this.matrix = matrix.multiply(this.matrix)
        return this
    }

    fun setMatrix(matrix: Matrix): ParticleObject {
        this.matrix = matrix
        return this
    }

    fun removeMatrix(): ParticleObject {
        matrix = null
        return this
    }

    fun hasMatrix(): Boolean {
        return matrix != null
    }

    fun getColor(): Color? {
        return if (data is ProxyParticle.DustData) {
            (data as ProxyParticle.DustData?)?.color
        } else null
    }

    fun setColor(color: Color) {
        data = ProxyParticle.DustData(color, 1f)
    }

    fun attachEntity(entity: Entity): ParticleObject {
        this.entity = entity
        return this
    }

    /**
     * 自定义程度较高的生成粒子方法
     *
     * @param location 坐标
     * @param particle 粒子
     * @param count 粒子数量
     * @param offset 偏移量
     * @param extra 粒子额外参数
     * @param data 特殊粒子属性
     */
    fun spawnParticle(
        location: Location,
        particle: ProxyParticle = this.particle,
        count: Int = this.count,
        offset: Vector = this.offset,
        extra: Double = this.extra,
        data: ProxyParticle.Data? = this.data,
    ) {
        var showLocation = location
        if (hasMatrix()) {
            val vector = location.clone().subtract(origin).toVector()
            val changed: Vector = matrix!!.applyVector(vector)
            showLocation = origin.clone().add(changed)
        }
        if (calcHit) {
            hitTargets.add(showLocation.toTarget().also {
                onHit?.let { hit -> hit(it) }
            })
            hitTargets.addAll(showLocation.getEntities().map { it.toTarget() }.apply {
                onHit?.let { hit -> forEach { hit(it) } }
            })
        }
        // 在这里可以设置一个XYZ的变化量
        showLocation.add(incrementX, incrementY, incrementZ)
        particle.sendTo(
            showLocation,
            offset = offset,
            count = count,
            speed = extra,
            data = data
        )
    }
}