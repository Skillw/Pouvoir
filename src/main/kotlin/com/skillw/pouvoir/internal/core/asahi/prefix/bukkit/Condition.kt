package com.skillw.pouvoir.internal.core.asahi.prefix.bukkit

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import com.skillw.pouvoir.Pouvoir.conditionManager
import org.bukkit.entity.LivingEntity

/**
 * @className Condition
 *
 * @author Glom
 * @date 2023/8/12 19:17 Copyright 2023 user. All rights reserved.
 */

@AsahiPrefix(["cond"])
private fun cond() = prefixParser {
    val entity = if (expect("of")) quest<LivingEntity>() else quester { selector() }
    when (val type = next()) {
        in arrayOf("test") -> {
            val key = questString()
            val params = (if (expect("with")) questMap() else quester { emptyMap<String, Any>() }).quester {
                it.mapValues { en ->
                    en.value ?: return@mapValues
                }
            }
            result {
                val cond = conditionManager[key.get()] ?: error("Unknown Condition ${key.get()}")
                cond.condition(entity.get(), params.get())
            }
        }

        else -> {
            error("Unknown Economy Operation $type")
        }
    }
}