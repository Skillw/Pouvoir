package com.skillw.pouvoir.internal.core.asahi.prefix.bukkit

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import taboolib.common.platform.function.console
import taboolib.common5.cbool
import taboolib.common5.cint
import taboolib.library.xseries.XPotion
import taboolib.module.lang.asLangText
import taboolib.module.nms.MinecraftVersion

/**
 * @className Entity
 *
 * @author Glom
 * @date 2023/1/14 0:32 Copyright 2023 user. All rights reserved.
 */
@AsahiPrefix(["damage"])
private fun damage() = prefixParser {
    val defenderGetter = quest<LivingEntity>()
    val amount = quest<Double>()
    val attackerGetter = if (expect("by")) quest<LivingEntity>() else quester { null }
    result {
        defenderGetter.get().run {
            attackerGetter.get()?.let {
                damage(amount.get(), it)
            } ?: damage(amount.get())
        }
    }
}

@AsahiPrefix(["potion"])
private fun potion() = prefixParser {
    //药水类型
    val typeGetter = quest<XPotion>()
    expect("in")
    //持续时间
    val duration = questTick().quester { it.toInt() }
    val map = if (peek() == "[" || peek() == "{") questTypeMap() else quester { emptyMap<String, Any>() }
    //等级
    val amplifier = map.quester { it.getOrDefault("level", 1).cint }
    //粒子可见性
    val ambient = map.quester { it.getOrDefault("effect", true).cbool }
    //图标
    val icon = map.quester { it.getOrDefault("icon", true).cbool }
    val entityGetter = if (expect("to")) quest<LivingEntity>() else quester { selector() }
    result {
        val xPotion = typeGetter.get()
        val type = xPotion.potionEffectType
        if (!xPotion.isSupported || type == null) {
            error(console().asLangText("asahi-potion-unsupportable", xPotion.name))
        }
        if (MinecraftVersion.majorLegacy >= 11300) {
            PotionEffect(type, duration.get(), amplifier.get(), ambient.get(), icon.get())
        } else {
            PotionEffect(type, duration.get(), amplifier.get(), ambient.get())
        }.apply(entityGetter.get())
    }
}