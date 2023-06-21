package com.skillw.particlelib.pobject

import com.skillw.pouvoir.util.getNearByEntities
import org.bukkit.entity.Entity
import taboolib.common.util.Location
import taboolib.common.util.Vector


/**
 * 代表一个射线
 *
 * @property direction 方向
 * @property maxLength 长度
 * @property step 步长
 * @property range 每个点判断怪物在其上的半径
 * @property stopType 结束类型
 * @property onHit 处理命中实体
 * @property entityFilter 命中实体过滤器
 * @author Zoyn IceCold
 */
class Ray constructor(
    origin: Location,
    var direction: Vector,
    var maxLength: Double,
    var step: Double = 0.2,
    var range: Double = 0.5,
    var stopType: RayStopType = RayStopType.MAX_LENGTH,
    var hitEntities: Boolean = false,
    var onHit: (Entity) -> Unit = { },
    var entityFilter: ((Entity) -> Boolean)? = null,
    var onEnd: (Entity) -> Unit = { },
) : ParticleObject(origin) {
    override fun show() {
        var currentLength = 0.0
        val entities = ArrayList<Entity>()
        while (currentLength < maxLength) {
            val vectorTemp = direction.clone().multiply(currentLength)
            val spawnLocation = origin.clone().add(vectorTemp)
            this.spawnParticle(spawnLocation)
            if (hitEntities) {
                val nearbyEntities = spawnLocation.getNearByEntities(range, range, range)
                // 检测有无过滤器
                if (entityFilter != null) {
                    for (entity in nearbyEntities) {
                        if (!entityFilter!!(entity)) {
                            entities.add(entity)
                        }
                    }
                } else {
                    entities.addAll(nearbyEntities)
                }
                // 获取首个实体
                if (entities.isNotEmpty() && stopType == RayStopType.HIT_FIRST_ENTITY) {
                    onHit(entities[0])
                    onEnd(entities[0])
                    break
                } else
                    entities.forEach(onHit)
            }
            currentLength += step
        }
        entities.forEach(onEnd)

    }

    enum class RayStopType {
        /** 固定长度(同时也是最大长度) */
        MAX_LENGTH,

        /** 碰撞至实体时停止 */
        HIT_FIRST_ENTITY,
    }
}