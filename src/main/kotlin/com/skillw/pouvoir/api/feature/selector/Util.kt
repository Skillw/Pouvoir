package com.skillw.pouvoir.api.feature.selector

import com.skillw.pouvoir.api.feature.selector.target.EntityTarget
import com.skillw.pouvoir.api.feature.selector.target.LocTarget
import org.bukkit.Location
import org.bukkit.entity.Entity
import taboolib.platform.util.toBukkitLocation

/**
 * @className Util
 *
 * @author Glom
 * @date 2023/1/22 15:51 Copyright 2023 user. All rights reserved.
 */

/**
 * 添加标记
 *
 * @param key String 标识符
 * @param durationTick Long 持续tick
 * @receiver Entity
 */
fun Entity.addFlag(key: String, durationTick: Long = -1) {
    EntityFlag.addFlag(this, key, durationTick)
}

/**
 * 获取拥有某个标记的所有实体
 *
 * @param flag 标识符
 * @return 结果
 */
fun getEntities(flag: String): Set<Entity> {
    return EntityFlag.getEntities(flag)
}

/**
 * 删除实体上的某个标记
 *
 * @param key 标识符
 */
fun Entity.removeFlag(key: String) {
    return EntityFlag.removeFlag(this, key)
}

/**
 * 转为目标
 *
 * @return
 */
fun Entity.toTarget(): EntityTarget {
    return EntityTarget(this)
}

/**
 * 转为目标
 *
 * @return
 */
fun Location.toTarget(): LocTarget {
    return LocTarget(this)
}

/**
 * 转为目标
 *
 * @return
 */
fun taboolib.common.util.Location.toTarget(): LocTarget {
    return LocTarget(this.toBukkitLocation())
}