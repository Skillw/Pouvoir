package com.skillw.pouvoir.api.feature.condition

import org.bukkit.entity.LivingEntity

/**
 * @className Condition
 *
 * @author Glom
 * @date 2022/7/18 23:55 Copyright 2022 user. All rights reserved.
 */
fun interface Condition {
    /**
     * 验证条件
     *
     * @param entity 实体 (可为null)
     * @param parameters 参数
     * @return 是否满足条件
     */
    fun condition(
        entity: LivingEntity?,
        parameters: Map<String, Any>,
    ): Boolean

}