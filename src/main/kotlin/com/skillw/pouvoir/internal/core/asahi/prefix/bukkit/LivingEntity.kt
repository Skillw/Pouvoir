package com.skillw.pouvoir.internal.core.asahi.prefix.bukkit

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import com.skillw.asahi.internal.util.Time
import com.skillw.pouvoir.util.attribute.BukkitAttribute
import com.skillw.pouvoir.util.nms.NMS
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import taboolib.common.platform.function.console
import taboolib.common5.cbool
import taboolib.common5.cint
import taboolib.library.xseries.XPotion
import taboolib.module.lang.asLangText
import taboolib.module.nms.MinecraftVersion
import java.util.concurrent.ConcurrentHashMap

/**
 * @className LivingEntity
 *
 * @author Glom
 * @date 2023/1/14 0:37 Copyright 2024 Glom.
 */
/**
 * @className Entity
 *
 * @author Glom
 * @date 2023/1/14 0:32 Copyright 2024 Glom.
 */
@AsahiPrefix(["damage"])
private fun damage() = prefixParser<Unit> {
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
private fun potion() = prefixParser<Any> {
    when (val operType = next()) {
        "add" -> {
            //药水类型
            val typeGetter = quest<XPotion>()
            val map = if (peek() == "[" || peek() == "{") questTypeMap() else quester { emptyMap<String, Any>() }
            //持续时间
            val duration = map.quester {
                it.getOrDefault("duration", 1).toString()
                    .run { toLongOrNull()?.let { tick -> Time.tick(tick) } ?: Time(this) }.toTick().toInt()
            }
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

        "has" -> {
            //药水类型
            val typeGetter = quest<XPotion>()
            //等级
            val amplifier = if (expect("level", "lv")) questInt() else quester { null }
            val entityGetter = if (expect("to")) quest<LivingEntity>() else quester { selector() }
            result {
                val xPotion = typeGetter.get()
                val type = xPotion.potionEffectType
                if (!xPotion.isSupported || type == null) {
                    error(console().asLangText("asahi-potion-unsupportable", xPotion.name))
                }
                val entity = entityGetter.get()
                val level = amplifier.get()
                level?.let {
                    entity.activePotionEffects.any {
                        (it.type == type) && (it.amplifier == level)
                    }
                } ?: entity.hasPotionEffect(type)
            }
        }

        "take" -> {
            //药水类型
            val typeGetter = quest<XPotion>()
            val map = if (peek() == "[" || peek() == "{") questTypeMap() else quester { emptyMap<String, Any>() }
            //持续时间
            val durationGetter = map.quester {
                it.getOrDefault("duration", 1).toString()
                    .run { toLongOrNull()?.let { tick -> Time.tick(tick) } ?: Time(this) }.toTick()
            }
            //等级
            val amplifier = map.quester { it.getOrDefault("level", 0).cint }
            val entityGetter = if (expect("to")) quest<LivingEntity>() else quester { selector() }
            result {
                val xPotion = typeGetter.get()
                val type = xPotion.potionEffectType
                if (!xPotion.isSupported || type == null) {
                    error(console().asLangText("asahi-potion-unsupportable", xPotion.name))
                }
                val entity = entityGetter.get()
                val effect = entity.getPotionEffect(type)
                entity.removePotionEffect(type)
                val duration = (effect?.duration ?: 0) - durationGetter.get()
                val level = (effect?.amplifier ?: 0) - amplifier.get()
                val ambient = effect?.isAmbient ?: true
                if (level <= 0 || duration <= 0) return@result
                PotionEffect(type, duration.toInt(), level, ambient).apply(entityGetter.get())
            }
        }

        else -> error("Unknown potion operation type $operType")
    }
}

private val cache = ConcurrentHashMap<String, AttributeModifier>()

@AsahiPrefix(["attribute"])
private fun attribute() = prefixParser<Any?> {
    val attributeGetter = quest<BukkitAttribute>()
    val instanceGetter = (if (expect("of")) quest<LivingEntity>() else quester { selector() }).quester {
        NMS.INSTANCE.getAttribute(
            it,
            attributeGetter.get()
        )
    }
    when (val operType = next()) {
        "base" -> when (next()) {
            "set" -> {
                expect("to")
                val value = questDouble()
                result {
                    instanceGetter.get()?.baseValue = value.get()
                }
            }

            else -> {
                result {
                    instanceGetter.get()?.baseValue
                }
            }
        }

        "add" -> {
            //属性类型
            val name = questString()
            val value = questDouble()
            val operation = quest<AttributeModifier.Operation>()
            result {
                val modifier =
                    cache.computeIfAbsent(name.get()) { AttributeModifier(name.get(), value.get(), operation.get()) }
                instanceGetter.get()?.addModifier(modifier)
            }
        }

        "has" -> {
            val name = questString()
            result {
                val require = name.get()
                instanceGetter.get()?.modifiers?.any { it.name == require }
            }
        }

        "remove" -> {
            val name = questString()
            result {
                val key = name.get()
                instanceGetter.get()?.apply {
                    modifiers?.filter { it.name == key }?.forEach { removeModifier(it) }
                }
            }
        }

        else -> error("Unknown potion operation type $operType")
    }
}