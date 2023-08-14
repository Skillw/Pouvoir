package com.skillw.pouvoir.api.feature.realizer.component

import com.skillw.pouvoir.api.feature.realizer.BaseRealizer

import com.skillw.pouvoir.util.calculateDouble
import org.bukkit.entity.LivingEntity

/**
 * @className Realizable
 *
 * @author Glom
 * @date 2023/1/5 16:25 Copyright 2022 user. All rights reserved.
 */
interface Valuable {
    val defaultValue: String

    fun value(entity: LivingEntity? = null): Double {
        return (this as? BaseRealizer)?.config?.getOrDefault("value", defaultValue).toString().calculateDouble(entity)
    }


}