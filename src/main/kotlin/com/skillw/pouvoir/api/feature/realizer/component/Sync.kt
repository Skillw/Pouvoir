package com.skillw.pouvoir.api.feature.realizer.component

import org.bukkit.entity.LivingEntity

/**
 * @className Sync
 *
 * @author Glom
 * @date 2023/8/4 21:32 Copyright 2023 user. All rights reserved.
 */
interface Sync {
    fun newTask(entity: LivingEntity): (() -> Unit)?
}