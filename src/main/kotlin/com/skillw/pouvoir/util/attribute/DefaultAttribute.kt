package com.skillw.pouvoir.util.attribute

import org.bukkit.Material
import taboolib.library.xseries.XMaterial

/**
 * 原版装备默认属性表 接受 item.getType(), 返回属性小数值 返回 0.0 则物品没有属性
 *
 * @author YiMiner
 * @version 1.1 Jun 25, 2021
 */
object DefaultAttribute {
    private val ATTACK_SPEED = HashMap<String, Double>()
    private val ATTACK_DAMAGE = HashMap<String, Double>()
    private val ARMOR = HashMap<String, Double>()
    private val ARMOR_TOUGHNESS = HashMap<String, Double>()
    private val KNOCKBACK_RESISTANCE = HashMap<String, Double>()

    @JvmStatic
    fun getAttackDamage(type: Material?): Double {
        return getAttackDamage(
            XMaterial.matchXMaterial(type!!.name).orElse(XMaterial.STONE)
        )
    }


    @JvmStatic
    fun getAttackSpeed(type: Material?): Double {
        return getAttackSpeed(
            XMaterial.matchXMaterial(type!!.name).orElse(XMaterial.STONE)
        )
    }


    @JvmStatic
    fun getArmor(type: Material?): Double {
        return getArmor(
            XMaterial.matchXMaterial(
                type!!
            )
        )
    }


    @JvmStatic
    fun getArmorToughness(type: Material?): Double {
        return getArmorToughness(
            XMaterial.matchXMaterial(type!!.name).orElse(XMaterial.STONE)
        )
    }


    @JvmStatic
    fun getKnockbackResistance(type: Material?): Double {
        return getKnockbackResistance(
            XMaterial.matchXMaterial(type!!.name).orElse(XMaterial.STONE)
        )
    }


    @JvmStatic
    fun getAttackDamage(type: XMaterial): Double {
        return getAttackDamage(type.name)
    }


    @JvmStatic
    fun getAttackSpeed(type: XMaterial): Double {
        return getAttackSpeed(type.name)
    }


    @JvmStatic
    fun getArmor(type: XMaterial): Double {
        return getArmor(type.name)
    }


    @JvmStatic
    fun getArmorToughness(type: XMaterial): Double {
        return getArmorToughness(type.name)
    }


    fun getKnockbackResistance(type: XMaterial): Double {
        return getKnockbackResistance(type.name)
    }


    @JvmStatic
    fun getAttackDamage(name: String): Double {
        return if (name.endsWith("_HOE")) {
            ATTACK_DAMAGE["HOE"]!!
        } else {
            ATTACK_DAMAGE.getOrDefault(name, 0.0)
        }
    }


    @JvmStatic
    fun getAttackSpeed(name: String): Double {
        return if (name.endsWith("_SWORD")) {
            ATTACK_SPEED["SWORD"]!!
        } else if (name.endsWith("_SHOVEL")) {
            ATTACK_SPEED["SHOVEL"]!!
        } else if (name.endsWith("_PICKAXE")) {
            ATTACK_SPEED["PICKAXE"]!!
        } else {
            ATTACK_SPEED.getOrDefault(name, 0.0)
        }
    }


    @JvmStatic
    fun getArmor(name: String): Double {
        return ARMOR.getOrDefault(name, 0.0)
    }


    @JvmStatic
    fun getArmorToughness(name: String): Double {
        return ARMOR_TOUGHNESS.getOrDefault(name, 0.0)
    }


    @JvmStatic
    fun getKnockbackResistance(name: String): Double {
        return KNOCKBACK_RESISTANCE.getOrDefault(name, 0.0)
    }


    @JvmStatic
    fun getDefault(type: Material?): Map<String, Double> {
        return getDefault(
            XMaterial.matchXMaterial(
                type!!
            )
        )
    }


    @JvmStatic
    fun getDefault(type: XMaterial): Map<String, Double> {
        return getDefault(type.name)
    }


    @JvmStatic
    fun getDefault(name: String): Map<String, Double> {
        val map: MutableMap<String, Double> = HashMap()
        val attackDamage = getAttackDamage(name)
        if (attackDamage > 0) {
            map["GENERIC_ATTACK_DAMAGE"] = attackDamage
        }
        val attackSpeed = getAttackSpeed(name)
        if (attackSpeed > 0) {
            map["GENERIC_ATTACK_SPEED"] = attackSpeed
        }
        val armor = getArmor(name)
        if (armor > 0) {
            map["GENERIC_ARMOR"] = armor
        }
        val armorToughness = getArmorToughness(name)
        if (armorToughness > 0) {
            map["GENERIC_ARMOR_TOUGHNESS"] = armorToughness
        }
        val knockbackResistance = getKnockbackResistance(name)
        if (knockbackResistance > 0) {
            map["GENERIC_KNOCKBACK_RESISTANCE"] = knockbackResistance
        }
        return map
    }

    init {
        // 攻击速度
        ATTACK_SPEED["SWORD"] = 1.6
        ATTACK_SPEED["TRIDENT"] = 1.1
        ATTACK_SPEED["SHOVEL"] = 1.0
        ATTACK_SPEED["PICKAXE"] = 1.2
        // 上述四类单独检索
        ATTACK_SPEED["WOODEN_AXE"] = 0.8
        ATTACK_SPEED["GOLDEN_AXE"] = 1.0
        ATTACK_SPEED["STONE_AXE"] = 0.8
        ATTACK_SPEED["IRON_AXE"] = 0.9
        ATTACK_SPEED["DIAMOND_AXE"] = 1.0
        ATTACK_SPEED["NETHERITE_AXE"] = 1.0
        ATTACK_SPEED["WOODEN_HOE"] = 1.0
        ATTACK_SPEED["GOLDEN_HOE"] = 1.0
        ATTACK_SPEED["STONE_HOE"] = 2.0
        ATTACK_SPEED["IRON_HOE"] = 3.0
        ATTACK_SPEED["DIAMOND_HOE"] = 4.0
        ATTACK_SPEED["NETHERITE_HOE"] = 4.0
        // 攻击伤害
        // 锄头单独处理
        ATTACK_DAMAGE["HOE"] = 1.0
        ATTACK_DAMAGE["TRIDENT"] = 9.0
        ATTACK_DAMAGE["WOODEN_SWORD"] = 4.0
        ATTACK_DAMAGE["GOLDEN_SWORD"] = 4.0
        ATTACK_DAMAGE["STONE_SWORD"] = 5.0
        ATTACK_DAMAGE["IRON_SWORD"] = 6.0
        ATTACK_DAMAGE["DIAMOND_SWORD"] = 7.0
        ATTACK_DAMAGE["NETHERITE_SWORD"] = 8.0
        // 铲子
        ATTACK_DAMAGE["WOODEN_SHOVEL"] = 2.5
        ATTACK_DAMAGE["GOLDEN_SHOVEL"] = 2.5
        ATTACK_DAMAGE["STONE_SHOVEL"] = 3.5
        ATTACK_DAMAGE["IRON_SHOVEL"] = 4.5
        ATTACK_DAMAGE["DIAMOND_SHOVEL"] = 5.5
        ATTACK_DAMAGE["NETHERITE_SHOVEL"] = 6.5
        // 稿子
        ATTACK_DAMAGE["WOODEN_PICKAXE"] = 2.0
        ATTACK_DAMAGE["GOLDEN_PICKAXE"] = 2.0
        ATTACK_DAMAGE["STONE_PICKAXE"] = 3.0
        ATTACK_DAMAGE["IRON_PICKAXE"] = 4.0
        ATTACK_DAMAGE["DIAMOND_PICKAXE"] = 5.0
        ATTACK_DAMAGE["NETHERITE_PICKAXE"] = 6.0
        // 斧子
        ATTACK_DAMAGE["WOODEN_AXE"] = 7.0
        ATTACK_DAMAGE["GOLDEN_AXE"] = 7.0
        ATTACK_DAMAGE["STONE_AXE"] = 9.0
        ATTACK_DAMAGE["IRON_AXE"] = 9.0
        ATTACK_DAMAGE["DIAMOND_AXE"] = 9.0
        ATTACK_DAMAGE["NETHERITE_AXE"] = 10.0
        // 盔甲值
        ARMOR["TURTLE_HELMET"] = 2.0
        // 皮
        ARMOR["LEATHER_HELMET"] = 1.0
        ARMOR["LEATHER_CHESTPLATE"] = 3.0
        ARMOR["LEATHER_LEGGINGS"] = 2.0
        ARMOR["LEATHER_BOOTS"] = 1.0
        // 金
        ARMOR["GOLDEN_HELMET"] = 2.0
        ARMOR["GOLDEN_CHESTPLATE"] = 5.0
        ARMOR["GOLDEN_LEGGINGS"] = 3.0
        ARMOR["GOLDEN_BOOTS"] = 1.0
        // 锁链
        ARMOR["CHAINMAIL_HELMET"] = 2.0
        ARMOR["CHAINMAIL_CHESTPLATE"] = 5.0
        ARMOR["CHAINMAIL_LEGGINGS"] = 4.0
        ARMOR["CHAINMAIL_BOOTS"] = 1.0
        // 铁
        ARMOR["IRON_HELMET"] = 2.0
        ARMOR["IRON_CHESTPLATE"] = 6.0
        ARMOR["IRON_LEGGINGS"] = 5.0
        ARMOR["IRON_BOOTS"] = 2.0
        // 钻
        ARMOR["DIAMOND_HELMET"] = 3.0
        ARMOR["DIAMOND_CHESTPLATE"] = 8.0
        ARMOR["DIAMOND_LEGGINGS"] = 6.0
        ARMOR["DIAMOND_BOOTS"] = 3.0
        // 合金
        ARMOR["NETHERITE_HELMET"] = 3.0
        ARMOR["NETHERITE_CHESTPLATE"] = 8.0
        ARMOR["NETHERITE_LEGGINGS"] = 6.0
        ARMOR["NETHERITE_BOOTS"] = 3.0
        // 盔甲韧性
        // 钻
        ARMOR_TOUGHNESS["DIAMOND_HELMET"] = 2.0
        ARMOR_TOUGHNESS["DIAMOND_CHESTPLATE"] = 2.0
        ARMOR_TOUGHNESS["DIAMOND_LEGGINGS"] = 2.0
        ARMOR_TOUGHNESS["DIAMOND_BOOTS"] = 2.0
        // 合金
        ARMOR_TOUGHNESS["NETHERITE_HELMET"] = 3.0
        ARMOR_TOUGHNESS["NETHERITE_CHESTPLATE"] = 3.0
        ARMOR_TOUGHNESS["NETHERITE_LEGGINGS"] = 3.0
        ARMOR_TOUGHNESS["NETHERITE_BOOTS"] = 3.0
        // 击退抗性
        // 合金
        KNOCKBACK_RESISTANCE["NETHERITE_HELMET"] = 0.1
        KNOCKBACK_RESISTANCE["NETHERITE_CHESTPLATE"] = 0.1
        KNOCKBACK_RESISTANCE["NETHERITE_LEGGINGS"] = 0.1
        KNOCKBACK_RESISTANCE["NETHERITE_BOOTS"] = 0.1
    }
}
