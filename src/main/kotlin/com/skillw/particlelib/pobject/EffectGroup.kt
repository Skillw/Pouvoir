package com.skillw.particlelib.pobject

import org.bukkit.entity.Entity
import taboolib.common.platform.ProxyParticle
import com.skillw.particlelib.utils.matrix.Matrixs

/**
 * 代表一个特效组
 *
 * 如果你要使用 EffectGroup#scale 这样的方法, 我不建议你将 2D 的特效和 3D 的特效放在一起
 *
 * @author Zoyn IceCold
 */
class EffectGroup {
    /** 特效表 */
    private val effectList: MutableList<ParticleObject>

    constructor() {
        effectList = ArrayList()
    }

    /**
     * 利用给定的特效列表构造出一个特效组
     *
     * @param effectList 特效列表
     */
    constructor(effectList: MutableList<ParticleObject>) {
        this.effectList = effectList
    }

    /**
     * 往特效组添加一项特效
     *
     * @param particleObj 特效对象
     * @return [EffectGroup]
     */
    fun addEffect(particleObj: ParticleObject): EffectGroup {
        effectList.add(particleObj)
        return this
    }

    /**
     * 往特效组添加一堆特效
     *
     * @param particleObj 一堆特效对象
     * @return [EffectGroup]
     */
    fun addEffect(vararg particleObj: ParticleObject): EffectGroup {
        effectList.addAll(particleObj.toList())
        return this
    }

    /**
     * 利用给定的下标, 将特效组里的第 index-1 个特效进行删除
     *
     * @param index 下标
     * @return [EffectGroup]
     */
    fun removeEffect(index: Int): EffectGroup {
        effectList.removeAt(index)
        return this
    }

    /**
     * 利用给定的数字, 设置每一个特效的循环 tick
     *
     * @param period 循环tick
     * @return [EffectGroup]
     */
    fun setPeriod(period: Long): EffectGroup {
        effectList.forEach { effect -> effect.period = period }
        return this
    }

    /**
     * 将特效组内的特效进行缩小或扩大
     *
     * @param value 缩小或扩大的倍率
     * @return [EffectGroup]
     */
    fun scale(value: Double): EffectGroup {
        effectList.forEach { effect -> effect.addMatrix(Matrixs.scale(2, 2, value)) }
        return this
    }

    /**
     * 将特效组内的特效进行旋转
     *
     * @param angle 旋转角度
     * @return [EffectGroup]
     */
    fun rotate(angle: Double): EffectGroup {
        effectList.forEach { effect -> effect.addMatrix(Matrixs.rotate2D(angle)) }
        return this
    }

    /**
     * 将特效组内的特效一次性展现出来
     *
     * @return [EffectGroup]
     */
    fun show(): EffectGroup {
        effectList.forEach { obj -> obj.show() }
        return this
    }

    /**
     * 将特效组内的特效一直地展现出来
     *
     * @return [EffectGroup]
     */
    fun alwaysShow(): EffectGroup {
        effectList.forEach { obj -> obj.alwaysShow() }
        return this
    }

    /**
     * 将特效组内的特效一直且异步地展现出来
     *
     * @return [EffectGroup]
     */
    fun alwaysShowAsync(): EffectGroup {
        effectList.forEach { obj -> obj.alwaysShowAsync() }
        return this
    }

    /**
     * 将特效组内的特效同时播放出来
     *
     * @return [EffectGroup]
     */
    fun play(): EffectGroup {
        for (pObj in effectList) {
            if (pObj is Playable) {
                val playable = pObj as Playable
                playable.play()
            }
        }
        return this
    }

    /**
     * 将特效组内的特效一直地同时播放出来
     *
     * @return [EffectGroup]
     */
    fun alwaysPlay(): EffectGroup {
        for (pObj in effectList) {
            if (pObj is Playable) {
                pObj.alwaysPlay()
            }
        }
        return this
    }

    /**
     * 将特效组内的特效一直且异步地同时播放出来
     *
     * @return [EffectGroup]
     */
    fun alwaysPlayAsync(): EffectGroup {
        for (pObj in effectList) {
            if (pObj is Playable) {
                pObj.alwaysPlayAsync()
            }
        }
        return this
    }

    fun attachEntity(entity: Entity): EffectGroup {
        effectList.forEach { effect -> effect.attachEntity(entity) }
        return this
    }

    fun setParticle(particle: ProxyParticle): EffectGroup {
        effectList.forEach { effect ->
            effect.particle = particle
        }
        return this
    }

    /**
     * 获取特效列表
     *
     * @return [List]
     */
    fun getEffectList(): List<ParticleObject> {
        return effectList
    }
}