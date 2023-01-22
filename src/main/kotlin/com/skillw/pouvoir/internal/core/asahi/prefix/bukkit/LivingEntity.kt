package com.skillw.pouvoir.internal.core.asahi.prefix.bukkit

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import org.bukkit.entity.LivingEntity

/**
 * @className LivingEntity
 *
 * @author Glom
 * @date 2023/1/14 0:37 Copyright 2023 user. All rights reserved.
 */

@AsahiPrefix(["permission"])
private fun permission() = prefixParser {
    val entityGetter = quest<LivingEntity>()
    val permissions = quest<Array<Any>>()
    result {
        val entity = entityGetter.get(); permissions.get().map { it.toString() }.all { entity.hasPermission(it) }
    }
}