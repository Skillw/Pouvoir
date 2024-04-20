package com.skillw.pouvoir.util.attribute

import org.bukkit.attribute.Attribute
import taboolib.common5.Coerce

/**
 * @className BukkitAttribute
 *
 * @author sky, Glom
 * @date 2023/8/4 22:37 Copyright 2024 Glom.
 */

enum class BukkitAttribute(val key: String, vararg val alias: String) {
    MAX_HEALTH("generic.max_health", "health", "max-health"),
    FOLLOW_RANGE("generic.follow_range", "follow", "follow-range"),
    KNOCKBACK_RESISTANCE("generic.knockback_resistance", "knockback", "knockback-resistance"),
    MOVEMENT_SPEED("generic.movement_speed", "speed", "movement-speed", "walk-speed"),
    FLYING_SPEED("generic.flying_speed", "fly-speed", "flying-speed"),
    ATTACK_DAMAGE("generic.attack_damage", "damage", "attack-damage"),
    ATTACK_KNOCKBACK("generic.attack_knockback", "damage-knockback", "attack-knockback"),
    ATTACK_SPEED("generic.attack_speed", "damage-speed", "attack-speed"),
    ARMOR("generic.armor", "armor"),
    ARMOR_TOUGHNESS("generic.armor_toughness", "toughness", "armor-toughness"),
    LUCK("generic.luck", "luck"),
    HORSE_JUMP_STRENGTH("horse.jump_strength", "jump-strength"),
    ZOMBIE_SPAWN_REINFORCEMENTS("zombie.spawn_reinforcements", "spawn-reinforcements");


    val normalizeName = name.lowercase().replace("_", "-")

    open fun parse(source: String): BukkitAttribute? = entries.firstOrNull { it.match(source) }


    fun     toBukkit(): Attribute? =
        Coerce.toEnum("GENERIC_$name", Attribute::class.java) ?: Coerce.toEnum(name, Attribute::class.java)


    fun match(source: String): Boolean {
        val lower = source.lowercase()
        return name == lower || lower in alias
    }
}